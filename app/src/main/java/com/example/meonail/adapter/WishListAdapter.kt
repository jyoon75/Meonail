package com.example.meonail.adapter

import android.content.Context
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

class WishListAdapter(
    private val context: Context,
    private val isWishList: Boolean
) : RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    private var wishItems: List<WishItem> = emptyList()
    private var onItemClickListener: ((WishItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (WishItem) -> Unit) {
        onItemClickListener = listener
    }

    fun updateData(newItems: List<WishItem>) {
        Log.d("WISH_LIST_UPDATE", "업데이트할 아이템 개수: ${newItems.size}")  // 🔥 데이터 개수 로그 추가

        wishItems = newItems
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_wish, parent, false)
        return WishViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        val item = wishItems[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = wishItems.size

    inner class WishViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imgThumbnail: ImageView = itemView.findViewById(R.id.imgWishThumbnail)
        private val txtTitle: TextView = itemView.findViewById(R.id.txtWishTitle)

        fun bind(item: WishItem) {
            txtTitle.text = item.title

            Glide.with(context)
                .load(item.imageObject)
                .placeholder(R.drawable.placeholder)
                .into(imgThumbnail)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item)
            }
        }
    }
}
