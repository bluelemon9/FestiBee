package com.example.festibee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocalMainRvAdapter(val context: Context, val bigareaCodeList:ArrayList<BigAreaCode>, val itemClick:(BigAreaCode)->Unit):
    RecyclerView.Adapter<LocalMainRvAdapter.Holder>(){
    //<> 안에 Holder 넣어줌
    //화면을 최초 로딩하여 만들어진 View가 없는 경우, xml파일을 inflate하여 ViewHolder를 생성한다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(context).inflate(R.layout.bigareacode_list,parent,false)
        return Holder(view,itemClick)
    }
    //RecyclerView로 만들어지는 item의 총 개수를 반환한다
    override fun getItemCount(): Int {
        return bigareaCodeList.size
    }
    //위의 OnCreateViewHolder에서 만든 view와 실제 입력되는 각각의 데이터를 연결
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder?.bind(bigareaCodeList[position],context)
    }

    inner class Holder(itemView: View?, itemClick:(BigAreaCode)->Unit): RecyclerView.ViewHolder(itemView!!){
        //각 View의 이름을 정하고, findViewById를 통해 ImageView인지 TextView인지 종류를 정하고
        //id를 통해 layout과 연결된다
        val bigareaCode = itemView?.findViewById<TextView>(R.id.bigareaView)

        //bind함수: ViewHolder와 클래스의 각 변수를 연동하는 역할함
        //이후에 Override 할 함수에서 사용할 것
        //이쪽 TextView엔 이 String을 넣어라 라고 지정하는 함수
        fun bind(bac:BigAreaCode, context: Context){
            bigareaCode?.text = bac.name.toString()

            itemView.setOnClickListener{itemClick(bac)}
        }
    }

}