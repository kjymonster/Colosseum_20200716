package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.co.tjoeun.colosseum_20200716.datas.Topic
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class ViewTopicDetailActivity : BaseActivity() {

    //메인화면에서 넘겨준 주제 id 저장
    var mTopicId = 0 //일단 Int임을 암시.  var로 해야 볼수 있음??

    //서버에서 받아오는 토론 정보를 저장할 변수
    lateinit var mTopic : Topic

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_topic_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {
        //main에서 넘겨문 id값을 멤버변수에 저장.
        //개발자는 0부터지만, db에서는 1부터임. 만약 0이 저장되었다면 오류가 있는 상황으로 간주
        mTopicId = intent.getIntExtra("topicId", 0) //없으면 0을 넘겨줘

        if(mTopicId == 0){
            Toast.makeText(mContext,"주제 상세 id에 문제가 있습니다.",Toast.LENGTH_SHORT).show()
        }

        //무사히 넘어왔다면, 서버에서 토론 주제에 대한 상제 진행 상황을 가져오기
        getTopicDetailFromServer() //코드가 길어서 따로 작업.
    }

    fun getTopicDetailFromServer(){

        ServerUtil.getRequestTopicDetail(mContext,mTopicId,object :ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

                val data = json.getJSONObject("data")
                val topicObj = data.getJSONObject("topic")

                //서버에서 내려주는 주제 정보를
                // Topic클래스 객체로 변환해서 멤버변수에 저장.
                // (상단에 멤버변수 생성  lateinit var mTopic : Topic)

                mTopic = Topic.getTopicFromJson(topicObj)

                //화면에 토론에 관련한 정보를 표시
                runOnUiThread{
                    
                }

            }
        })

    }


}