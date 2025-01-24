package com.example.meonail.adapter

import android.content.Context
import android.content.SharedPreferences
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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WishListAdapter(private val context: Context) : RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    private val items = mutableListOf<WishItem>()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("WishPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val favoriteItems = loadFavorites().toMutableSet() // 🔥 찜한 항목을 저장하는 Set

    private var onItemClickListener: ((WishItem) -> Unit)? = null

    fun updateData(newItems: List<WishItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (WishItem) -> Unit) {
        onItemClickListener = listener
    }

    inner class WishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgThumbnail: ImageView = view.findViewById(R.id.imgWishItemThumbnail)
        private val txtTitle: TextView = view.findViewById(R.id.txtWishItemTitle)
        private val imgFavorite: ImageView = view.findViewById(R.id.imgWishFavorite)

        fun bind(item: WishItem, clickListener: ((WishItem) -> Unit)?) {
            txtTitle.text = item.title
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgThumbnail)

            val itemKey = gson.toJson(item) // 🔥 WishItem 객체를 JSON 문자열로 변환

            // 🔥 찜 상태에 따라 하트 아이콘 변경
            imgFavorite.setImageResource(
                if (favoriteItems.contains(itemKey)) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )

            // 🔥 찜 버튼 클릭 이벤트
            imgFavorite.setOnClickListener {
                if (favoriteItems.contains(itemKey)) {
                    favoriteItems.remove(itemKey) // 찜 해제
                    imgFavorite.setImageResource(R.drawable.ic_favorite_border)
                } else {
                    favoriteItems.add(itemKey) // 찜 추가
                    imgFavorite.setImageResource(R.drawable.ic_favorite_filled)
                }
                saveFavorites(favoriteItems) // 🔥 SharedPreferences 저장
            }

            // ✅ 클릭 시 이벤트 실행
            itemView.setOnClickListener {
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

    // 🔥 SharedPreferences에서 찜한 목록 불러오기 (JSON -> 객체 변환)
    private fun loadFavorites(): MutableSet<String> {
        return sharedPreferences.getStringSet("favorite_items", emptySet()) ?: mutableSetOf()
    }

    // 🔥 SharedPreferences에 찜한 목록 저장
    private fun saveFavorites(favorites: MutableSet<String>) {
        sharedPreferences.edit().putStringSet("favorite_items", favorites).apply()
    }
}
