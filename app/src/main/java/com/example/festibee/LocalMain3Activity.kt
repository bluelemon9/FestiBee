package com.example.festibee

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_local_main3.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class LocalMain3Activity : AppCompatActivity() {
    var sigunguCode: Int ?= null
    var code:Int ?= null
    var baseAreaInfoList = arrayListOf<BaseAreaInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_main3)

        getXml().execute()

        if(intent.hasExtra("sigunguCode")){
            sigunguCode = intent.getStringExtra("sigunguCode").toInt()
        }else{
            Toast.makeText(this,"전달된 시군구값이 없습니다",Toast.LENGTH_SHORT).show()
        }

        if(intent.hasExtra("code")){
            code = intent.getStringExtra("code").toInt()
        }else{
            Toast.makeText(this,"전달된 짱지역값이 없습니다",Toast.LENGTH_SHORT).show()
        }

        val m2Adapter = LocalMain3RvAdapter(this,baseAreaInfoList){BaseAreaInfo ->
            val nextIntent = Intent(this,LocalMain4Activity::class.java)
            nextIntent.putExtra("contentid","${BaseAreaInfo.content_id}")
            Toast.makeText(this,"ItemClick: ${BaseAreaInfo.content_id}",Toast.LENGTH_SHORT).show()
            startActivity(nextIntent)
        }
        RecyclerView2.adapter = m2Adapter

        //레이아웃 매니저(LayoutManager)
        //RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을 때 재사용할 것인지 결정하는 역할
        //item을 재사용할 때, LayoutManager는 Adapter에게 view의 요소를 다른 데이터로 대체할 것인지 물어봄
        val lm = LinearLayoutManager(this)
        RecyclerView2.layoutManager = lm

        //item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수도 있고,
        //그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있음 -> setHasFixedSize
        RecyclerView2.setHasFixedSize(true)

    }

    inner class getXml : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var key = "w1f4oQHnmOzGNSV%2BmjRGJlB0%2Btf7brZzgSPWGhy6yTiyKmjEz4hxAwsS4LLs4kxS5k%2FwaSMqK7CrbFyZpHpa5g%3D%3D"
            var url = URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaBasedList?ServiceKey=${key}&MobileOS=AND&MobileApp=AppTest&arrange=A&contentTypeId=15&areaCode=${code}&sigunguCode=${sigunguCode}").openStream()
            var xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()
            var temp = "Root element: ${xml.documentElement.nodeName}\n"
            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")
            lateinit var tempBAI:BaseAreaInfo

            for (i in 0..list.length - 1) {
                var n: Node = list.item(i)
                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    val map = mutableMapOf<String, String>()

                    for (j in 0..elem.attributes.length - 1) {
                        map.putIfAbsent(
                            elem.attributes.item(j).nodeName,
                            elem.attributes.item(j).nodeValue
                        )
                    }
                    //temp += "==========${i + 1}===========\n"
                    //temp += "1.주소: ${elem.getElementsByTagName("addr1").item(0).textContent}\n"
                    //temp += "2.행사 이름: ${elem.getElementsByTagName("title").item(0).textContent}\n"
                    tempBAI = BaseAreaInfo("${elem.getElementsByTagName("title").item(0).textContent}","${elem.getElementsByTagName("contentid").item(0).textContent}".toInt())
                }
               baseAreaInfoList.add(i, tempBAI)
            }
            return temp
        }

        override fun onPostExecute(result: String?) {
            //textView3.text = result.toString()
        }

    }
}
