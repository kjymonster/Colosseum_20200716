package kr.co.tjoeun.colosseum_20200716.utils

import android.content.Context

class ContextUtil {

    //sharedPreferences 구현 (간단한 데이터 항목 저장, 자동 로그인 등)
    companion object {   //ContextUtil을 하나의 기능으로 사용하기 위해 companion object{}사용

        //메모장의 파일 이름처럼, sharedPreferences의 이름을 변수로 저장.
        private val preName = "ColosseumPref" //이름 저장해 주기. (프로젝트이름 + Pref)

        //편리한 자동완성을 위해(오타 줄이기 위해), 저장해줄 데이터 항목 이름도 변수로 저장하는 것이 좋음.
        private val LOGIN_USER_TOKEN = "LOGIN_USER_TOKEN"

        //setter -> 데이터(토근값)를 저장하는 기능
        fun setLoginUserToken(context: Context, token:String){

            //메모장(pref)을 먼저 열고,
            val pref = context.getSharedPreferences(preName,Context.MODE_PRIVATE)

            //열린 메모장에 token 변수를 저장.
            pref.edit().putString(LOGIN_USER_TOKEN, token).apply() //LOGIN_USER_TOKEN에 token을 가져와라.

        }

        //getter -> 저장된 토큰을 리턴해주는 기능
        fun getLoginUserToken(context: Context) : String{
            //메모장 열기(상동)
            val pref = context.getSharedPreferences(preName,Context.MODE_PRIVATE)
            return pref.getString(LOGIN_USER_TOKEN, "") !! // (, " " = 저장된게 없으면 줄 값) + !!
        }



    }
}