package com.example.festibee

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.review_list.view.*
import java.util.*

class ReviewAdapter (val context: Context)
    : RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {
    private var items = ArrayList<ReviewData>()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(viewGroup.context)
        val itemView: View = inflater.inflate(R.layout.review_list, viewGroup, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val item: ReviewData = items[position]
        viewHolder.setItem(item)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    fun setItems(items: ArrayList<ReviewData>) {
        this.items = items
    }

    inner class ViewHolder(itemView: View)
        : RecyclerView.ViewHolder(itemView) {
        fun setItem(item: ReviewData) {
            itemView.tv_review.text = item.review
            //itemView.ratingBar.rating = item.star!!.toFloat()
            itemView.ratingBar.setRating(item.star!!.toFloat())
        }
    }
}