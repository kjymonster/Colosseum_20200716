package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.ViewReplyDetailActivity
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.utils.TimeUtil

class ReplyAdapter (val mContext: Context,
                    resId : Int,
                    val mList:List<Reply>) : ArrayAdapter<Reply>(mContext,resId,mList) {

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
        selectedSideTitleTxt.text = "(%(data.selectedSide.title))"
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


        //답글 버튼이 눌리면, 의견 상세화면으로 진입
        replyBtn.setOnClickListener {

            val myIntent = Intent(mContext, ViewReplyDetailActivity::class.java)
            //startActivity 함수는 AppCompatActivity 가 내려주는 기능.
            //Adapter는 액티비티가 아니므로, startActivity 기능을 내려주지 않는다.
            //mContext 변수가 어떤 화면이 리스트뷰를 뿌리는지 들고 있다.
            //mContext를 이용해서 액티비티를 열어주자.
            mContext.startActivity(myIntent)

        }



        return row

    }
}