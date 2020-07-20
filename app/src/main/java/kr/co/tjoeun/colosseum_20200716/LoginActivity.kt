package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kr.co.tjoeun.colosseum_20200716.utils.ContextUtil
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class LoginActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setupEvents()
        setValues()
    }


    override fun setupEvents() {

        signUpBtn.setOnClickListener {
            val myIntent = Intent(mContext,SignUpActivity::class.java)
            startActivity(myIntent)
        }

        loginBtn.setOnClickListener {

            //입력한 아이디와 비번을 받아서
            val inputEmail = emailEdt.text.toString()
            val inputPw = pwEdt.text.toString()

            //서버에 전달 해 주고 응답 처리(응답처리 보류 = null)
            ServerUtil.postRequestLogin(
                mContext,
                inputEmail,
                inputPw,
                object : ServerUtil.JsonResponseHandler {
                    override fun onResponse(json: JSONObject) {

                        //로그인이 성공/실패 여부는 code에 적혀있는 Int값으로 구별할 수 있도록 서버 코드구성.
                        //200 : 로그인 성공
                        //그 외 모든 숫자 : 로그인 실패

                        val codeNum = json.getInt("code")
                        val reasonFail = json.getString("message")

                        if (codeNum == 200) {
                            //로그인 성공 -> 서버가 알려주는 토큰을 반영구 저장.

                            //json 안의 data 안의 token 스트링 추출 과정
                            val data = json.getJSONObject("data") //중괄호를 추출할때 : getJSONObject
                            val token = data.getString("token") //미리 만든 data 변수의 String 가져오기.

                            //얻어낸 토큰을 저장.
                            ContextUtil.setLoginUserToken(mContext, token)

                            //메인 화면으로 이동 -> 로그인 화면은 종료처리(finish()) (MainActivity 생성 후)
                            val myIntent = Intent(mContext,MainActivity::class.java)
                            startActivity(myIntent)

                            finish()



                        } else {
                            //로그인 실패 -> 토스트로 실패했다고 출력
                            //어떤 이유로 실패했는지 서버가 주는 메세지를 출력

                            runOnUiThread {
                                Toast.makeText(mContext, reasonFail, Toast.LENGTH_SHORT).show()
                                Toast.makeText(mContext, reasonFail, Toast.LENGTH_SHORT).show()
                            }


                        }


                    }
                })

        }
    }


    override fun setValues() {

    }


}



