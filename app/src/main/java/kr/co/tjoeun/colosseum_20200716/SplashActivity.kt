package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kr.co.tjoeun.colosseum_20200716.utils.ContextUtil

class SplashActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

    }

    override fun setValues() {

        val myHandler = Handler()

        myHandler.postDelayed({
            //2.5초 후에 저장된 토큰이 있다면 메인 화면으로 이동
            //토큰이 빈칸이라면, 로그인 필요하다고 판단. 로그인 화면으로 가도록.

            if(ContextUtil.getLoginUserToken(mContext) == ""){
                //로그인 화면으로
                val myIntent = Intent(mContext,LoginActivity::class.java)
                startActivity(myIntent)
            }else{
                val myIntent = Intent(mContext,MainActivity::class.java)
                startActivity(myIntent)
            }
            //로딩화면 종료. (더이상 할일이 X)
             finish()

        }, 2500)

    }


}