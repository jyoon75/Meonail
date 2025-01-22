package com.example.meonail.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meonail.R
import com.example.meonail.model.WishItem

class WishListAdapter : RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    private val items = mutableListOf<WishItem>()
    private var onItemClickListener: ((WishItem) -> Unit)? = null // ✅ 클릭 리스너 추가

    fun updateData(newItems: List<WishItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
        Log.d("WishListAdapter", "updateData: ${items.size} 개의 아이템이 추가됨")
    }

    // ✅ 클릭 리스너 설정 함수 추가
    fun setOnItemClickListener(listener: (WishItem) -> Unit) {
        onItemClickListener = listener
    }

    class WishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgThumbnail: ImageView = view.findViewById(R.id.imgWishItemThumbnail)
        private val txtTitle: TextView = view.findViewById(R.id.txtWishItemTitle)

        fun bind(item: WishItem, clickListener: ((WishItem) -> Unit)?) {
            txtTitle.text = item.title
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgThumbnail)

            // ✅ 클릭 시 이벤트 실행
            itemView.setOnClickListener {
                Log.d("WishListAdapter", "아이템 클릭됨: ${item.title}") // 🔹 로그 확인
                clickListener?.invoke(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wish, parent, false)
        return WishViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(items[position], onItemClickListener)
    }

    override fun getItemCount(): Int = items.size
}
