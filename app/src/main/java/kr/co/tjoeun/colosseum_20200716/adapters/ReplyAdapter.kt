package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.ViewReplyDetailActivity
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil
import org.json.JSONObject

class ReplyAdapter(
    val mContext: Context,
    resId: Int,
    val mList: List<Reply>
) : ArrayAdapter<Reply>(mContext, resId, mList) {

    val inf = LayoutInflater.from(mContext)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var tempRow = convertView   //convertView에는 나중에 직접 값을 저장하는 것이 불가능.
        if (tempRow == null) {
            tempRow = inf.inflate(R.layout.reply_list_item, null)
        }

        val row = tempRow!!

        val writerNicknameTxt = row.findViewById<TextView>(R.id.writerNicknameTxt)
        val selectedSideTitleTxt = row.findViewById<TextView>(R.id.selectedSideTitleTxt)
        val contentTxt = row.findViewById<TextView>(R.id.contentTxt)

        //(0724) 시간 정보 텍스트뷰
        val replyWriteTimeTxt = row.findViewById<TextView>(R.id.replyWriteTimeTxt)

        //버튼들 추가 -> 좋아요/싫어요/답글
        val replyBtn = row.findViewById<Button>(R.id.replyBtn)
        val likeBtn = row.findViewById<Button>(R.id.likeBtn)
        val dislikeBtn = row.findViewById<Button>(R.id.dislikeBtn)




        val data = mList[position]

        writerNicknameTxt.text = data.writer.nickname
        selectedSideTitleTxt.text = "${data.selectedSide.title}"
        contentTxt.text = data.content

        //시간 정보 텍스트뷰 내용 설정 => 방금 전, ?분 전, ?시간 전 등등을 표현

        replyWriteTimeTxt.text = TimeUtil.getTimeAgoFromCalendar(data.writtenDateTime)


        //날짜 출력 양식용 변수
//        val sdf = SimpleDateFormat("yy-MM-dd a h시 m분")
//
//        replyWriteTimeTxt.text = sdf.format(data.writtenDateTime.time)

        //좋아요/싫어요/답글 갯수 반영
        likeBtn.text = "좋아요 ${data.likeCount}"
        dislikeBtn.text = "싫어요 ${data.dislikeCount}"
        replyBtn.text = "답글 ${data.replyCount}"

        //내 좋아요 여부 / 내 싫어요 여부도 반영
        if(data.myLike) {
            //좋아요 버튼의 배경을 -> red_border_box.xml로 변경
            likeBtn.setBackgroundResource(R.drawable.red_border_box)
            //좋아요 버튼의 글씨 색깔도 -> naverRed로 변경
            likeBtn.setTextColor(mContext.resources.getColor(R.color.naverRed))
        }else {
            //좋아요 버튼의 배경을 -> gray_border_box.xml로 변경
            likeBtn.setBackgroundResource(R.drawable.gray_border_box)
            //좋아요를 안 찍었다면 -> gray로 돌려줘야함.
            likeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }

        if(data.myDisLike) {
            //싫어요를 누른 상태
            dislikeBtn.setBackgroundResource(R.drawable.blue_border_box)
            dislikeBtn.setTextColor(mContext.resources.getColor(R.color.naverBlue))
        }else {
            dislikeBtn.setBackgroundResource(R.drawable.gray_border_box)
            likeBtn.setTextColor(mContext.resources.getColor(R.color.textGray))
        }



        //답글 버튼이 눌리면, 의견 상세화면으로 진입
        replyBtn.setOnClickListener {

            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)
            //startActivity 함수는 AppCompatActivity 가 내려주는 기능.
            //Adapter는 액티비티가 아니므로, startActivity 기능을 내려주지 않는다.
            //mContext 변수가 어떤 화면이 리스트뷰를 뿌리는지 들고 있다.
            //mContext를 이용해서 액티비티를 열어주자.


            //몇번 의견에 대한 상세를 보고싶은지 id만 넘겨주자
            //해당 화면에서 다시 서버를 통해 데이터를 받아오자.
            myIntent.putExtra("replyId", data.id)
            mContext.startActivity(myIntent)

        }

        //의견에 대한 좋아요 / 싫어요 버튼 클릭 이벤트
        likeBtn.setOnClickListener {

            ServerUtil.postRequestLikeOrDisLike(
                mContext,
                data.id,
                true,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {

                        //변경된 좋아요/싫어요의 갯수를 파악해서
                        //버튼 문구를 새로 반영.
                        // -> 목록에 뿌려지는 data의 좋아요/싫어요 갯수를 변경

                        val dataObj = json.getJSONObject("data")
                        val replyObj = dataObj.getJSONObject("reply")

                        val reply = Reply.getReplyFromJson(replyObj)

                        //이미 화면에 뿌려져 있는 data의 내용만 교체
                        data.likeCount = reply.likeCount
                        data.dislikeCount = reply.dislikeCount

                        //내가 좋아요를 찍었는지? 아닌지? 다시 체크
                        data.myLike = reply.myLike
                        data.myDisLike = reply.myDisLike


                        //data의 값이 변경 => 리스트뷰를 구성하는 목록에 변경
                        // => 어댑터.notifyDatasetChanged 실행해야함. (목록의 일부분만 변경되어도)
                        //이 코드는 어댑터 내부이므로, notifyDatasetChanged가 내장되어 있다.
                        // => 단순히 호출만 하면 끝.

                        //새로고침도 UI변경이므로 runOnUiThread 등으로 UI쓰레드가 처리하도록 해야함.
                        //단, 어댑터에는 runOUiThread 기능이 내장되어 있지 않다.

                        //Handler를 이용해서 => UI쓰레드에 접근하게 하자. (안드로이드가 제공하는 핸들러)

                        val uiHandler = Handler(Looper.getMainLooper())
                        uiHandler.post {
                            notifyDataSetChanged()

                        //서버가 알려주는 메세지를 토스트로 출력
                            val message = json.getString("message")
                            Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show()
                        }





                    }
                })

        }


        //싫어요 버튼 누르면 => 서버에 전달처리 + (좋아요/싫어요) 갯수 반영 / 토스트로 출력
        dislikeBtn.setOnClickListener {
            ServerUtil.postRequestLikeOrDisLike(mContext, data.id,false, object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                    val dataObj = json.getJSONObject("data")
                    val replyObj = dataObj.getJSONObject("reply")

                    val reply = Reply.getReplyFromJson(replyObj)

                    data.dislikeCount = reply.dislikeCount
                    data.likeCount = reply.likeCount

                    data.myLike = reply.myLike
                    data.myDisLike = reply.myDisLike

                    val uiHandler = Handler(Looper.getMainLooper())

                    uiHandler.post {
                        notifyDataSetChanged()

                        //서버가 알려주는 메세지를 토스트로 출력
                        val message = json.getString("message")
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show()


                    }

                }
            })

        }

        return row

    }
   }