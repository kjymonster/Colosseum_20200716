package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_view_reply_detail.*
import kotlinx.android.synthetic.main.activity_view_reply_detail.selectedSideTitleTxt
import kotlinx.android.synthetic.main.activity_view_reply_detail.writerNicknameTxt
import kr.co.tjoeun.colosseum_20200716.adapters.ReReplyAdapter
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject

class ViewReplyDetailActivity : BaseActivity() {

    //보려는 의견의 id는 여러 함수에서 공유할 것 같다.
    //그래서 멤버변수로 만들고 저장한다.
    var mReplyId = 0

    //이 화면에서 보여줘야할 의견의 정보를 가진 변수 => 멤버변수로 만들어준다.
    //var mReply =
    lateinit var mReply: Reply


    //의견에 달린 답글들을 저장할 목록
    val mReReplyList = ArrayList<Reply>()

    lateinit var mReReplyAdapter : ReReplyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_reply_detail)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        postReReplyBtn.setOnClickListener {
            val inputContent = contentEdt.text.toString()

            //5글자 미만이면, 거부처리

            if(inputContent.length < 5) {
                Toast.makeText(mContext,"최소 5글자 이상은 입력해주세요", Toast.LENGTH_SHORT).show()
                //이 뒤의 코드는 실행할 필요가 없음.
                return@setOnClickListener
            }

            //굳이 else를 안 써도 5글자 이상임이 담보된 상태.
            //적은 내용을 답글로 서버에 등록
            ServerUtil.postRequestReReply(mContext, mReplyId, inputContent, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    //서버가 이야기하는 내용을 출력
                runOnUiThread {

                    val message = json.getString("message")
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()
                }
                    //의견 상테를 다시 불러내서 -> 답글 목록도 다시 받아내자.
                    getReplyFromServer()

                }
            })

        }

    }

    override fun setValues() {

        //의견 리스트뷰에서 보내주는 id값을 멤버변수에 담아주자.
        mReplyId = intent.getIntExtra("replyId", 0)

        // 해당 id값에 맞는 의견 정보를 서버에서 다시 불러오자
        getReplyFromServer()

        //대댓글 어댑터 초기화, 리스트뷰 연결
       mReReplyAdapter = ReReplyAdapter(mContext,R.layout.re_reply_list_item,mReReplyList)
        reReplyListView.adapter = mReReplyAdapter
    }

    //서버에서 의견 정보 불러오기
    fun getReplyFromServer() {

        //액티비티가 ServerUtil에게 일을 시키는 것 (ServerUtil에서는 액티비티에 일을 시키는 것)
        ServerUtil.getRequestReplyDetail(
            mContext,
            mReplyId,
            object : ServerUtil.JsonResponseHandler {
                override fun onResponse(json: JSONObject) {

                    val data = json.getJSONObject("data")
                    val replyObj = data.getJSONObject("reply")

                    //replyObj 를 Reply클래스로 변환한 결과를 mReply에 저장
                    mReply = Reply.getReplyFromJson(replyObj)

                    //답글 목록은 누적되면 안됨 -> 다 비워주고 다시 파싱
                    mReReplyList.clear()

                    //replies JSONArray를 돌면서 => Replay로 변환해서 => mReReplayList에 추가


                    val replies = replyObj.getJSONArray("replies")
                    for (i in 0 until replies.length()){

                        val reply = Reply.getReplyFromJson(replies.getJSONObject(i))

                        mReReplyList.add(reply)

                    }


                        //mReply 내부의 변수(정보)들을 -> 화면(UI)에 반영
                        runOnUiThread {
                            writerNicknameTxt.text = mReply.writer.nickname
                            selectedSideTitleTxt.text = "(${mReply.selectedSide.title})"
                            writtenDateTimeTxt.text =
                                TimeUtil.getTimeAgoFromCalendar(mReply.writtenDateTime)
                            replyContentTxt.text = mReply.content

                            //답글 목록이 모두 불러지면 새로 반영
                            mReReplyAdapter.notifyDataSetChanged()

                        }




                }

            })



    }



}