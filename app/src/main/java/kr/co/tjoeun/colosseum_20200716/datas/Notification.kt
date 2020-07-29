package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class Notification {

    var id = 0
    var title = ""

    //알림이 발생한 시간을 기록할 Calendar 변수
    val createdAtCal = Calendar.getInstance()

    companion object {

        //SimpleDataFormat은 고정양식 -> 한번만 만들고 재활용
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        fun getNotificationFromJson(json : JSONObject) :Notification {
            val n = Notification()

            n.id = json.getInt("id")
            n.title = json.getString("title")

            //발생한 시간 => 양식에 맞춰서 String => Calendar로 변환


            n.createdAtCal.time = sdf.parse(json.getString("created_at"))

            val myPhoneTimeZone = n.createdAtCal.timeZone
            val timeOffSet = myPhoneTimeZone.rawOffset / 1000 / 60 / 60

            n.createdAtCal.add(Calendar.HOUR, timeOffSet)

            return n
        }
    }
}