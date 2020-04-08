package com.example.festibee

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_local_main4.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class LocalMain4Activity : AppCompatActivity() {

    private var firestore : FirebaseFirestore? = null
    private var auth: FirebaseAuth? = null
    private var adapter: ReviewAdapter? = null

    var title:String ?= null
    var addr:String ?= null
    var date:String ?= null
    var eventPlace:String ?= null
    var useTime:String ?= null
    var agelimit:String ?= null
    var subevent:String ?= null
    var sponsor1:String ?= null
    var sponsor2:String ?= null
    var overView:String ?= null

    var contentid:Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_main4)

        titleVIew.text = " "
        addrView.text = " "
        dateView.text = " "
        eventPlaceView.text = " "
        usetimeView.text = " "
        agelimitView.text = " "
        subeventView.text = " "
        sponsor1View.text = " "
        sponsor2View.text = " "
        overViewView.text = " "

        auth = FirebaseAuth.getInstance()

        getXml().execute()

        if(intent.hasExtra("contentid")){
            contentid = intent.getStringExtra("contentid").toInt()
            Toast.makeText(this,"ItemClick: ${contentid}",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this,"전달된 contentid값이 없습니다", Toast.LENGTH_SHORT).show()
        }

        // 리뷰 별점 등록
        val rBar = findViewById<RatingBar>(R.id.ratingBar2)
        if (rBar != null) {
            val button = findViewById<Button>(R.id.button)
            button?.setOnClickListener {
                val msg = rBar.rating.toString()
                Toast.makeText(
                    this,"별점: " + msg, Toast.LENGTH_SHORT
                ).show()

                addDatabase()
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ReviewAdapter(this)
        recyclerView.adapter = adapter

        viewDatabase()
    }

    private fun addDatabase() {     //추가
        if (editText.text.isEmpty()) {
            textView7.text = "입력되지 않은 값이 있습니다."
            return
        }

        var user = FirebaseAuth.getInstance().currentUser
        var document: String? = null
        if (user != null) {
            document = user.uid //사용자ID 받아와서 document 이름으로 만들기
        } else {
            document = "no_user"
        }

        val rBar = findViewById<RatingBar>(R.id.ratingBar2)
        val reviewDT = ReviewData(rBar.rating, editText.text.toString())

        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(contentid.toString())?.document(document)?.set(reviewDT)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show()
                    viewDatabase()  // 리뷰 목록 새로고침
                } else {
                    textView7.text = task.exception?.message
                }
            }
    }

    private fun viewDatabase() {        //저장된 리스트 보여주기
        firestore = FirebaseFirestore.getInstance()
        firestore?.collection(contentid.toString())?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var reviewList = ArrayList<ReviewData>()
                    for (dc in task.result!!.documents) {
                        var reviewDTO = dc.toObject(ReviewData::class.java)
                        reviewList.add(reviewDTO!!)
                    }
                    adapter?.setItems(reviewList)
                    adapter?.notifyDataSetChanged()
                }
                else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_LONG).show()
                }
            }
    }

    inner class getXml : AsyncTask<Void, Void, String>() {
        override fun doInBackground(vararg params: Void?): String {

            var key = "w1f4oQHnmOzGNSV%2BmjRGJlB0%2Btf7brZzgSPWGhy6yTiyKmjEz4hxAwsS4LLs4kxS5k%2FwaSMqK7CrbFyZpHpa5g%3D%3D"
            //url은 공통정보
            //url2은 소개정보
            var url = URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailCommon?serviceKey=${key}&MobileOS=AND&MobileApp=AppTest&contentId=${contentid}&contentTypeId=15&defaultYN=Y&firstImageYN=Y&areacodeYN=Y&catcodeYN=Y&addrinfoYN=Y&mapinfoYN=Y&overviewYN=Y").openStream()
            var url2 = URL("http://api.visitkorea.or.kr/openapi/service/rest/KorService/detailIntro?serviceKey=${key}&MobileOS=AND&MobileApp=AppTest&contentId=${contentid}&contentTypeId=15").openStream()

            var xml: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url)
            var xml2: Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url2)

            xml.documentElement.normalize()
            xml2.documentElement.normalize()

            var temp = "Root element: ${xml.documentElement.nodeName}\n"
            //var temp2 = "Root element: ${xml2.documentElement.nodeName}\n"

            //찾고자 하는 데이터가 어느 노드 아래에 있는지 확인
            val list: NodeList = xml.getElementsByTagName("item")
            val list2: NodeList = xml2.getElementsByTagName("item")

            for (i in 0..list.length-1) {
                var n: Node = list.item(i)
                var n2:Node = list2.item(i)

                if (n.getNodeType() == Node.ELEMENT_NODE) {
                    val elem = n as Element
                    val elem2 = n2 as Element

                    val map = mutableMapOf<String, String>()
                    val map2 = mutableMapOf<String, String>()

                    title = "${elem.getElementsByTagName("title").item(0).textContent}"
                    addr = "${elem.getElementsByTagName("addr1").item(0).textContent}"
                    date = "${elem2.getElementsByTagName("eventstartdate").item(0).textContent}~${elem2.getElementsByTagName("eventenddate").item(0).textContent}"
                    eventPlace = "${elem2.getElementsByTagName("eventplace").item(0).textContent}"
                    useTime = "${elem2.getElementsByTagName("usetimefestival").item(0).textContent}"
                    agelimit = "${elem2.getElementsByTagName("agelimit").item(0).textContent}"
                    subevent = "${elem2.getElementsByTagName("subevent").item(0).textContent}"
                    sponsor1 = "${elem2.getElementsByTagName("sponsor1").item(0).textContent}"
                    sponsor2 = "${elem2.getElementsByTagName("sponsor2").item(0).textContent}, ${elem2.getElementsByTagName("sponsor2tel").item(0).textContent} "
                    overView = "${elem.getElementsByTagName("overview").item(0).textContent}"
                }
            }
            return temp
        }

        override fun onPostExecute(result: String?) {
            titleVIew.text = title.toString()
            addrView.text = addr.toString()
            dateView.text = date.toString()
            eventPlaceView.text = eventPlace
            usetimeView.text = useTime
            agelimitView.text = agelimit
            subeventView.text = subevent
            sponsor1View.text = sponsor1
            sponsor2View.text = sponsor2
            overViewView.text = overView
        }
    }
}
