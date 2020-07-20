package kr.co.tjoeun.colosseum_20200716

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.view.*
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        okBtn.setOnClickListener {

            //닉네임과 이메일은 비어있으면 안된다.
            val inputEmail = emailEdt.text.toString()

            if(inputEmail.isEmpty()){
                //비어 있는 경우
                Toast.makeText(mContext,"이메일은 반드시 입력해야 합니다.",Toast.LENGTH_SHORT).show()
                //이메일이 비어있으면 아래 비밀번호 등 코드는 실행할 필요가 없다.
                //이벤트 처리를 강제 종료시킨다. (return)
                return@setOnClickListener
            }

            val inputNickname = nickNameEdt.text.toString()

            if (inputNickname.isEmpty()){
                    //비어 있는 경우
                    Toast.makeText(mContext,"닉네임은 반드시 입력해야 합니다.",Toast.LENGTH_SHORT).show()
                    //닉네임이 비어있으면 아래 비밀번호 검사 코드는 실행할 필요가 없다.
                    //이벤트 처리를 강제 종료시킨다. (return)
                    return@setOnClickListener
                }


            //비밀번호는 8글자 이내면 안됨.
            val inputPassword = passwordEdt.text.toString()
            if(inputPassword.length < 8){
                Toast.makeText(mContext,"비밀번호는 반드시 최소 8글자 이상이어야 합니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //모든 검사를 통과하면 서버에 가입 요청처리
            //여기의 코드가 실행된다는 것은 모든 검사를 통과했다는 말. (return)

            //ServerUtil.  -> 회원가입처리 해야함.
            ServerUtil.putRequestSignUp(mContext,inputEmail,inputPassword,inputNickname,object : ServerUtil.JsonResponseHandler{
                override fun onResponse(json: JSONObject) {

                }

            })

        }

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