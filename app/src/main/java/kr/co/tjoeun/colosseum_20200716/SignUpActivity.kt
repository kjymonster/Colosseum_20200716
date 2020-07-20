package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        //비밀번호 입력 내용 변경 이벤트 처리
        passwordEdt.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, start: Int, p2: Int, p3: Int) {
                //내용 변경 완료된 시점에서 실행.
              //  Log.d("비번이 입력됨", p0.toString())
                //입력된 글자의 길이 확인.
                //비어있다면, "비밀번호를 입력해 주세요."
                //8글자가 안되면, "비밀번호가 너무 짧습니다."
                //8글자 이상이면, "사용해도 좋은 비밀번호 입니다."

                val tempPw = passwordEdt.text.toString()

                //if(tempPw.length == 0)
                if(tempPw.isEmpty()){
                    //입력 안한 경우
                    passwordCheckResultTxt.text = "비밀번호를 입력해 주세요."
                }else if(tempPw.length < 8){
                    //길이가 부족한 경우
                    passwordCheckResultTxt.text = "비밀번호가 너무 짧습니다."
                }else{
                    //비어있지도 않고, 길이가 짧지도 않음. 충분히 긴 비밀번호임.
                    passwordCheckResultTxt.text = "사용해도 좋은 비밀번호 입니다."
                }
            }

        })

    }

    override fun setValues() {

    }


}