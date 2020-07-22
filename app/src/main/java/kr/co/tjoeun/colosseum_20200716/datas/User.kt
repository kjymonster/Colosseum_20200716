package kr.co.tjoeun.colosseum_20200716.datas

import org.json.JSONObject

class User {

    var id = 0
    var email = ""
    var nickname = ""

    companion object{

        fun getUserFromJson(json : JSONObject) : User {
            val u = User()



            return u

        }

    }

}