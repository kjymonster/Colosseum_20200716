package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject

class Topic {
    var id = 0 //0으로 넣어서 Int가 될것이라고 암시
    var title = "" //빈칸으로 둬서 String이 될것이라고 암시
    var imageUrl = ""

    //data폴더에 Side 만들고 와서.
    //주제는 선택 진영 목록을 하위 정보로 갖는다.
    val sideList = ArrayList<Side>()

    // (0722) 내가 투표한 진영의 id와 데이터를 저장해주자.
    var mysideId = -1
    var mySide : Side? = null //내가 투표한 진영은 없을 수도(null) 있다. (null이 가능하도록)




    companion object {
        //json 한 덩어리를 넣으면 => Topic 객체로 변환해주는 기능
        fun getTopicFromJson(json: JSONObject): Topic {

            //변환시켜줄 Topic객체를 미리 생성
            val topic = Topic()

            //만든 객체의 내용물들을 json을 이용해서 채우자.
            topic.id = json.getInt("id") //json에서 "id"라는 이름의 Int 가져오기
            topic.title = json.getString("title") //json에서 "title"이라는 이름의 String 가져오기
            topic.imageUrl = json.getString("img_url") //json에서 img_url"이라는 이름의 String 가져오기
            //side 작업 후.
            //sides 배열에 들어있는 진영 선택 정보도 넣어줘야함.
            val sides = json.getJSONArray("sides")

            //받아낸 jsonArray 내부를 스캔
            for(i in 0 until sides.length()){

                //진영 정보를 하나씩 파싱해서 -> 토론의 진영 목록에 추가
                val sideObj = sides.getJSONObject(i)
                val side = Side.getSideFromJson(sideObj)

                topic.sideList.add(side)


            }

            //내가 선택한 진영 관련 정보 파싱
            topic.mysideId = json.getInt("my_side_id")
            //서버에서 my_side에 진영 정보를 넣어줄때만 파싱. (if문 활용)
            if(!json.isNull("my_side")) {  //! = NOT 연산자

                val mySideJson = json.getJSONObject("my_side")
                topic.mySide = Side.getSideFromJson(mySideJson)

            }


            //완성된 객체를 리턴.
            return topic

            //메인 액티비티에 이어서. (07/21)

        }

    }
}