package kr.co.tjoeun.colosseum_20200716

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.tjoeun.colosseum_20200716.adapters.TopicAdapter
import kr.co.tjoeun.colosseum_20200716.datas.Topic
import kr.co.tjoeun.colosseum_20200716.utils.ServerUtil
import org.json.JSONObject
import java.util.ArrayList

class MainActivity : BaseActivity(){

    val mTopicList = ArrayList<Topic>()

    lateinit var mTopicAdapter : TopicAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupEvents()
        setValues()
    }

    override fun setupEvents() {

        //토론 주제를 누르면 -> 상세화면으로 이동
        topicListView.setOnItemClickListener { parent, view, position, id ->

            //눌린 위치에 맞는 주제를 가져오자
            val clickedTopic = mTopicList[position]

            //상세화면으로 진입 => 클릭된 주제의 id값만 화면에 전달
            val myIntent = Intent(mContext, ViewTopicDetailActivity::class.java)
            myIntent.putExtra("topicId", clickedTopic.id)
            startActivity(myIntent)

        }



    }

    override fun setValues() {
        getTopicListFromServer()

        mTopicAdapter = TopicAdapter(mContext,R.layout.topic_list_item,mTopicList)
        topicListView.adapter = mTopicAdapter
    }

    fun getTopicListFromServer(){  //너무 길어서 setValues밖에서 작업
        ServerUtil.getRequestMainInfo(mContext,object :ServerUtil.JsonResponseHandler{
            override fun onResponse(json: JSONObject) {

               val data = json.getJSONObject("data")

                //분석기에서 topics는 대괄호 [ ] 임, == jsonArray (data에게 호출)
                val topics = data.getJSONArray("topics")

                //topics내부에는 JSONObject들이 여러개 반복으로 들어있다.
                //그래서 JSON을 들고있는 배열이므로, JSONArray

                //for문을 이용해서, topics 내부의 데이터를 하나씩 추출.
                for(i in 0..topics.length()-1){  // .. == until (alr+enter)
                    //i가 0부터 ~topics의 갯수 직전. (4개 : (0,1,2,3))

                    //topics내부의 데이터를 JSONObject로 추출
                    val topicObj = topics.getJSONObject(i) //topics한테 JSONObject를 꺼내옴.

                    //topicObj를 Topic 형태의 객체로 변환.
                    val topic = Topic.getTopicFromJson(topicObj)

                    //변환된 객체를 목록에 추가
                    mTopicList.add(topic)
                     //Topic(data)로 (7/21)
                }

                //for문으로 주제 목록을 모두 추가하고 나면 리스트뷰의 내용이 바뀌었다고 새로고침
                runOnUiThread{
                    mTopicAdapter.notifyDataSetChanged() //아답터에게 데이터가 바뀌었다고 알려줌
                }


            }

        })
    }


}