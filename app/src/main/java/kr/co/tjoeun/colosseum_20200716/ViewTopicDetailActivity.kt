package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kr.co.tjoeun.colosseum_20200716.datas.Topic
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    //메인화면에서 넘겨준 주제 id 저장
    var mTopicId = 0 //일단 Int임을 암시.  var로 해야 볼수 있음??

    //서버에서 받아오는 토론 정보를 저장할 변수
    lateinit var mTopic: Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {
        //투표 버튼까지 만들었으면 여기서 작업.
        //버튼이 눌리면 할 일을 변수에 담아서 저장.
        // (TedPermission에서 권한별 할 일을 변수에 담아서 저장한 것과 같은 논리)


        //투표를 하는 코드라는 의미에서 voteCode
        val voteCode = View.OnClickListener {

            //it : View -> 눌린 버튼을 담고 있는 변수.
            val clickedSideTag = it.tag.toString()

            Log.d("눌린 버튼의 태그", clickedSideTag)

            //눌린 버튼의 태그를 Int로 바꿔서,
            // 토론 주제의 진영 중 어떤 진영을 눌렀는지 가져오는 index로 활용
            val clickedSide = mTopic.sideList[clickedSideTag.toInt()]

            Log.d(" 투표하려는 진영 제목",clickedSide.title)

            //실제로 해당 진영에 투표하기
            ServerUtil.postRequestVote(mContext,clickedSide.id, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                }

            })
        }

        //두 개의 투표하기 버튼이 눌리면 할 일을 모두 voteCode에 적힌 내용으로 설정.
        voteToFirstSideBtn.setOnClickListener(voteCode)  //이 버튼이 눌리면 할일은 voteCode에 있다.
        voteToSecondSideBtn.setOnClickListener(voteCode)

        }



    override fun setValues() {
        //main에서 넘겨문 id값을 멤버변수에 저장.
        //개발자는 0부터지만, db에서는 1부터임. 만약 0이 저장되었다면 오류가 있는 상황으로 간주
        mTopicId = intent.getIntExtra("topicId", 0) //없으면 0을 넘겨줘

        if (mTopicId == 0) {
            Toast.makeText(mContext, "주제 상세 id에 문제가 있습니다.", Toast.LENGTH_SHORT).show()
        }

        //무사히 넘어왔다면, 서버에서 토론 주제에 대한 상제 진행 상황을 가져오기
        getTopicDetailFromServer() //코드가 길어서 따로 작업.
    }

    fun getTopicDetailFromServer() {

        ServerUtil.getRequestTopicDetail(
            mContext,
            mTopicId,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(json: JSONObject) {

                    val data = json.getJSONObject("data")
                    val topicObj = data.getJSONObject("topic")

                    //서버에서 내려주는 주제 정보를
                    // Topic클래스 객체로 변환해서 멤버변수에 저장.
                    // (상단에 멤버변수 생성  lateinit var mTopic : Topic)

                    mTopic = Topic.getTopicFromJson(topicObj)

                    //화면에 토론에 관련한 정보를 표시
                    runOnUiThread {

                        topicTitleTxt.text = mTopic.title
                        Glide.with(mContext).load(mTopic.imageUrl).into(topicImg)

                        //data - Side - Topic에 작업 완료 후
                        //진영 정보도 같이 표시
                        firstSideTitleTxt.text = mTopic.sideList[0].title
                        secondSideTitleTxt.text = mTopic.sideList[1].title

                        firstSideVoteCountTxt.text = "${mTopic.sideList[0].voteCount}표"
                        secondSideVoteCountTxt.text = "${mTopic.sideList[1].voteCount}표"


                    }

                }
            })

    }

}
