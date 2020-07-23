package kr.co.tjoeun.colosseum_20200716.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import kr.co.tjoeun.colosseum_20200716.R
import kr.co.tjoeun.colosseum_20200716.datas.Reply
import kr.co.tjoeun.colosseum_20200716.datas.Topic

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

        val data = mList[position]

        writerNicknameTxt.text = data.writer.nickname
        selectedSideTitleTxt.text = "(%(data.selectedSide.title))"
        contentTxt.text = data.content

        //시간 정보 텍스트뷰 내용 설정 => 방금 전, ?분 전, ?시간 전 등등을 표현


        return row

    }
}