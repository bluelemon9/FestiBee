package com.example.festibee

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_local_main.*
import kotlinx.android.synthetic.main.activity_local_main2.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class LocalMainActivity : AppCompatActivity() {

    var bigareaCodeList = arrayListOf<BigAreaCode>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_main)
        getXml().execute()

        //Adapter 생성
        val mAdapter = LocalMainRvAdapter(this,bigareaCodeList){BigAreaCode ->
            val nextIntent = Intent(this,LocalMain2Activity::class.java)
            nextIntent.putExtra("code","${BigAreaCode.bigareaCode}")
            Toast.makeText(this,"ItemClick: ${BigAreaCode.name}", Toast.LENGTH_SHORT).show()
            startActivity(nextIntent)
        }
        RecyclerView1.adapter = mAdapter

        //레이아웃 매니저(LayoutManager)
        //RecyclerView의 각 item들을 배치하고, item이 더이상 보이지 않을 때 재사용할 것인지 결정하는 역할
        //item을 재사용할 때, LayoutManager는 Adapter에게 view의 요소를 다른 데이터로 대체할 것인지 물어봄
        val lm = LinearLayoutManager(this)
        RecyclerView1.layoutManager = lm

        //item이 추가되거나 삭제될 때 RecyclerView의 크기가 변경될 수도 있고,
        //그렇게 되면 계층 구조의 다른 View 크기가 변경될 가능성이 있음 -> setHasFixedSize
        RecyclerView1.setHasFixedSize(true)
    }


    inner class getXml : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {
            var key = "w1f4oQHnmOzGNSV%2BmjRGJlB0%2Btf7brZzgSPWGhy6yTiyKmjEz4hxAwsS4LLs4kxS5k%2FwaSMqK7CrbFyZpHpa5g%3D%3D"
            var url = URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/areaCode?serviceKey=${key}&numOfRows=25&MobileOS=AND&MobileApp=AppTest").openStream()
            var xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            xml.documentElement.normalize()
            var temp = "Root element: ${xml.documentElement.nodeName}\n"
            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")
            lateinit var tempBAC:BigAreaCode

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
                    tempBAC = BigAreaCode("${elem.getElementsByTagName("code").item(0).textContent}".toInt(),"${elem.getElementsByTagName("name").item(0).textContent}")
                }
                bigareaCodeList.add(i, tempBAC)
            }
            return temp
        }

        override fun onPostExecute(result: String?) {
            //textView3.text = result.toString()
        }

    }
}
