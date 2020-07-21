package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject

class Topic {
    var id = 0 //0으로 넣어서 Int가 될것이라고 암시
    var title = "" //빈칸으로 둬서 String이 될것이라고 암시
    var imageUrl = ""

    //json 한 덩어리를 넣으면 => Topic 객체로 변환해주는 기능

    companion object {
        fun getTopicFromJson(json: JSONObject): Topic {

            //변환시켜줄 Topic객체를 미리 생성
            val topic = Topic()

            //만든 객체의 내용물들을 json을 이용해서 채우자.
            topic.id = json.getInt("id") //json에서 "id"라는 이름의 Int 가져오기
            topic.title = json.getString("title") //json에서 "title"이라는 이름의 String 가져오기
            topic.imageUrl = json.getString("img_url") //json에서 img_url"이라는 이름의 String 가져오기

            //완성된 객체를 리턴.
            return topic

            //메인 액티비티에 이어서. (07/21)

        }

    }
}