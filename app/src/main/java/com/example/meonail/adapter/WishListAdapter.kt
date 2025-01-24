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

class WishListAdapter(
    private val context: Context,
    private val isWishList: Boolean,
    private var onWishRemovedListener: ((WishItem) -> Unit)? = null // ✅ 여기에만 선언
) : RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    private val items = mutableListOf<WishItem>()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WishPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun updateData(newItems: List<WishItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnWishRemovedListener(listener: (WishItem) -> Unit) {
        onWishRemovedListener = listener
    }

    inner class WishViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgThumbnail: ImageView = view.findViewById(R.id.imgWishItemThumbnail)
        private val txtTitle: TextView = view.findViewById(R.id.txtWishItemTitle)
        private val imgFavorite: ImageView = view.findViewById(R.id.imgWishFavorite)

        fun bind(item: WishItem) {
            txtTitle.text = item.title
            Glide.with(itemView.context)
                .load(item.imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imgThumbnail)

            // ✅ 🔥 위시리스트에서는 항상 하트가 채워진 상태
            if (isWishList) {
                imgFavorite.setImageResource(R.drawable.ic_favorite_filled)

                // ✅ 🔥 위시리스트에서만 삭제 가능
                imgFavorite.setOnClickListener {
                    removeWishItem(item)
                    onWishRemovedListener?.invoke(item) // 🔥 삭제 이벤트 전달
                }
            } else {
                // ✅ 🔥 위시탭에서는 SharedPreferences에서 상태를 불러와 설정
                val isFavorite = isFavoriteItem(item)
                imgFavorite.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite_filled
                    else R.drawable.ic_favorite_border
                )

                imgFavorite.setOnClickListener {
                    toggleFavorite(item, imgFavorite)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wish, parent, false)
        return WishViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    // ✅ 🔥 위시리스트에서만 삭제 (위시탭은 건드리지 않음)
    private fun removeWishItem(item: WishItem) {
        items.remove(item)
        saveFavorites(items) // ✅ SharedPreferences에 반영
        notifyDataSetChanged()
    }

    // ✅ 🔥 SharedPreferences에 저장된 찜 목록 불러오기
    private fun loadFavorites(): MutableList<WishItem> {
        val json = sharedPreferences.getString("favorite_items", null)
        val type = object : TypeToken<MutableList<WishItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    // ✅ 🔥 SharedPreferences에 찜 목록 저장
    private fun saveFavorites(favorites: List<WishItem>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString("favorite_items", json)
        editor.apply()
    }

    // ✅ 🔥 특정 아이템이 찜 목록에 있는지 확인
    private fun isFavoriteItem(item: WishItem): Boolean {
        return loadFavorites().any { it.title == item.title }
    }

    // ✅ 🔥 찜 추가/해제 기능 (위시탭에서만 실행)
    private fun toggleFavorite(item: WishItem, imgFavorite: ImageView) {
        val favorites = loadFavorites()
        if (favorites.any { it.title == item.title }) {
            favorites.removeAll { it.title == item.title }
            imgFavorite.setImageResource(R.drawable.ic_favorite_border)
        } else {
            favorites.add(item)
            imgFavorite.setImageResource(R.drawable.ic_favorite_filled)
        }
        saveFavorites(favorites)
    }
}
