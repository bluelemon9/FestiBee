package com.example.festibee

import android.content.Intent
import android.hardware.Camera
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_local_main2.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.net.URLEncoder
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.math.ln

class LocalMain2Activity() : AppCompatActivity(){

    var areaCodeList = arrayListOf<AreaCode>()
    var code:Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_main2)

        getXml().execute()

        if(intent.hasExtra("code")){
            code = intent.getStringExtra("code").toInt()
        }else{
            Toast.makeText(this,"전달된 지역값이 없습니다",Toast.LENGTH_SHORT).show()
        }

        //Adapter 생성
        val mAdapter = LocalMain2RvAdapter(this,areaCodeList){AreaCode ->
            val nextIntent = Intent(this,LocalMain3Activity::class.java)
            nextIntent.putExtra("code","${code}")
            nextIntent.putExtra("sigunguCode","${AreaCode.areaCode}")
            Toast.makeText(this,"ItemClick: ${AreaCode.name}",Toast.LENGTH_SHORT).show()
            startActivity(nextIntent)
        }
        RecyclerView.adapter = mAdapter

        //레이아웃 매니저(LayoutManager)
        //RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을 때 재사용할 것인지 결정하는 역할
        //item을 재사용할 때, LayoutManager는 Adapter에게 view의 요소를 다른 데이터로 대체할 것인지 물어봄
        val lm = LinearLayoutManager(this)
        RecyclerView.layoutManager = lm

        //item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수도 있고,
        //그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있음 -> setHasFixedSize
        RecyclerView.setHasFixedSize(true)
    }


    inner class getXml : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var key = "w1f4oQHnmOzGNSV%2BmjRGJlB0%2Btf7brZzgSPWGhy6yTiyKmjEz4hxAwsS4LLs4kxS5k%2FwaSMqK7CrbFyZpHpa5g%3D%3D"
            var url = URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode?serviceKey=${key}&numOfRows=25&MobileOS=AND&MobileApp=AppTest&areaCode=${code}").openStream()
            var xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()
            var temp = "Root element: ${xml.documentElement.nodeName}\n"
            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")
            lateinit var tempAC:AreaCode

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
                    //temp += "1.지역번호: ${elem.getElementsByTagName("code").item(0).textContent}\n"
                    //temp += "2.시군구 이름: ${elem.getElementsByTagName("name").item(0).textContent}\n"
                    tempAC = AreaCode("${elem.getElementsByTagName("code").item(0).textContent}".toInt(),"${elem.getElementsByTagName("name").item(0).textContent}")
                }
                areaCodeList.add(i, tempAC)
            }
            return temp
        }

        override fun onPostExecute(result: String?) {
            //textView3.text = result.toString()
        }

    }
}


