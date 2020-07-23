package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_edit_reply.*
import kotlinx.android.synthetic.main.activity_view_topic_detail.*
import kotlinx.android.synthetic.main.activity_view_topic_detail.topicTitleTxt
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class EditReplyActivity :BaseActivity() {

    //어떤 토론에 대한 의견을 다는지 알려주는 멤버변수
    var mTopicId = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_reply)
    }

    override fun setupEvents() {


        postBtn.setOnClickListener {

            //입력한 내용을 저장
            val inputContent = contentEdt.text.toString()

            ServerUtil.postRequestReply(mContext, mTopicId, inputContent,object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                }


            } )

        }



    }

    override fun setValues() {


        topicTitleTxt.text = intent.getStringExtra("topicTitle")
        mySideTitleTxt.text = intent.getStringExtra("selectedSideTitle")

        //몇번토론에 대한 의견 작성인지 변수로 저장.
        mTopicId = intent.getIntExtra("topicId", 0)  //Int 데이터가 없으면 0으로 초기화


    }


}