package com.example.festibee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class LocalMain3RvAdapter (val context: Context, val baseAreaInfoList:ArrayList<BaseAreaInfo>, val itemClick:(BaseAreaInfo)->Unit):
    RecyclerView.Adapter<LocalMain3RvAdapter.Holder2>(){
    //<> 안에 Holder 넣어줌
    //화면을 최초 로딩하여 만들어진 View가 없는 경우, xml파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder2 {
        val view = LayoutInflater.from(context).inflate(R.layout.baseareainfo_list,parent,false)
        return Holder2(view,itemClick)
    }
    //RecyclerView로 만들어지는 item의 총 개수를 반환한다
    override fun getItemCount(): Int {
        return baseAreaInfoList.size
    }

    //위의 OnCreateViewHolder에서 만든 view와 실제 입력되는 각각의 데이터를 연결
    override fun onBindViewHolder(holder: Holder2, position: Int) {
        holder?.bind(baseAreaInfoList[position],context)
    }

    inner class Holder2(itemView: View?, itemClick:(BaseAreaInfo)->Unit): RecyclerView.ViewHolder(itemView!!){
        //각 View의 이름을 정하고, findViewById를 통해 ImageView인지 TextView인지 종류를 정하고
        //id를 통해 layout과 연결된다
        val event_name = itemView?.findViewById<TextView>(R.id.eventName)
        val content_id = itemView?.findViewById<TextView>(R.id.contentIdView)
        //val image_path = itemView?.findViewById<ImageView>(R.id.imageView)

        //bind함수: ViewHolder와 클래스의 각 변수를 연동하는 역할함
        //이후에 Override 할 함수에서 사용할 것
        //이쪽 TextView엔 이 String을 넣어라 라고 지정하는 함수
        fun bind(bai:BaseAreaInfo, context: Context){
            event_name?.text = bai.event_name.toString()
            content_id?.text = bai.content_id.toString()
            //Glide.with(context).load(bai.image_path.toString()).into(image_path!!)
            itemView.setOnClickListener{itemClick(bai)}
        }
    }

}

