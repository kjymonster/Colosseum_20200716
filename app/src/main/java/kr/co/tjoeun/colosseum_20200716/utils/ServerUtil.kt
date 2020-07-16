package kr.co.tjoeun.colosseum_20200716.utils

import android.content.Context
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.io.IOException

class ServerUtil {

    //서버의 응답을 처리해주는 용도로 쓰는 인터페이스
    interface JsonResponseHandler {

        fun onResponse(json: JSONObject)
    }

    //Java의 static에 대응되는 개념 (companion object{})
    companion object {

        //호스트 주소를 매번 타이핑 하기 싫어서 저장해두는 변수
        private val BASE_URL = "http://15.165.177.142"

        //로그인 API를 호출해주는 기능  (mContext아님/ ~Handler? = null이 담길수도 있는 가능성을 열어둠)
        //1. 화면에서 어떤 데이터를 받아와야 하는지? email, pw (함수의 재료로)
        fun postRequestLogin(context : Context, email: String, pw:String, handeler: JsonResponseHandler?) {

            //서버 통신 담당 변수 (클라이언트 역할 수행 변수)
            val client = OkHttpClient()

            //어느 주소로 가야하는지 저장 (로그인은 http://15.165.177.142/user로 가야함)
            val urlString = "${BASE_URL}/user"

            //서버에 가지고 갈 짐(데이터들)을 FormBody를 이용해서 담자.
            //POST / PUT / PATCH가 같은 방식을 사용.
            val formData = FormBody.Builder()
                .add("email", email)
                .add("password", pw)
                .build() //마무리(.build() )

            //실제로 요청 정보를 종합하는 변수 Request 생성
            //Intent 만드는것과 비슷한 개념임.
            val request = Request.Builder()
                .url(urlString)
                .post(formData)
                .build()



            //종합된 request 변수를 이용해서 실제 API 호출(Intent의 startActivity)
            //(누가? client가)
            //받아온 응답도 같이 처리

            client.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    //서버 연결 자체에 실패한 경우
                }

                override fun onResponse(call: Call, response: Response) {
                   //연결은 성공해서, 서버가 응답을 내려줬을 때 실행됨.(아직 로그인 성공/실패가 아님)
                    
                    //실제로 서버가 내려준 응답 내용을 변수로 저장. (응답내용 = body)
                    val bodyStr = response.body?.string() //그냥 string(), body는 null일수도 있음
                    
                    //응답 내용으로 Json 객체 생성
                    val json = JSONObject(bodyStr)
                    
                    //서버에서 최종적으로 가져온 내용을 로그로 출력해보기
                    Log.d("서버 응답 내용", json.toString())
                    
                    //handler 변수에 응답처리 코드가 들어있다면 실행해주자.
                    handeler?.onResponse(json) //handler? -> 핸들러가 null이 아닐때만 실행
                    
                }

            })


        }

    }

}