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
    private var onWishRemovedListener: ((WishItem) -> Unit)? = null
) : RecyclerView.Adapter<WishListAdapter.WishViewHolder>() {

    private var wishItems: MutableList<WishItem> = mutableListOf()
    private var onItemClickListener: ((WishItem) -> Unit)? = null

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WishPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()


    fun setOnItemClickListener(listener: (WishItem) -> Unit) {
        onItemClickListener = listener
    }

    fun updateData(newItems: List<WishItem>) {
        Log.d("WISH_LIST_UPDATE", "업데이트할 아이템 개수: ${newItems.size}")  // 🔥 데이터 개수 로그 추가

        wishItems.clear()
        wishItems.addAll(newItems)
        notifyDataSetChanged()
    }

    fun setOnWishRemovedListener(listener: (WishItem) -> Unit) {
        onWishRemovedListener = listener
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
        private val imgFavorite: ImageView = itemView.findViewById(R.id.imgWishFavorite)

        fun bind(item: WishItem) {
            txtTitle.text = item.title

            Glide.with(context)
                .load(item.imageObject)
                .placeholder(R.drawable.placeholder)
                .into(imgThumbnail)

            itemView.setOnClickListener {
                onItemClickListener?.invoke(item)
            }

            // ✅ 위시리스트에서는 하트가 채워진 상태
            if (isWishList) {
                imgFavorite.setImageResource(R.drawable.ic_favorite_filled)

                // ✅ 위시리스트에서 삭제 가능
                imgFavorite.setOnClickListener {
                    removeWishItem(item)
                    onWishRemovedListener?.invoke(item)
                }
            } else {
                // ✅ 위시탭에서 찜 여부 확인
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
    private fun removeWishItem(item: WishItem) {
        wishItems.remove(item)
        saveFavorites(wishItems)
        notifyDataSetChanged()
    }

    private fun loadFavorites(): MutableList<WishItem> {
        val json = sharedPreferences.getString("favorite_items", null)
        val type = object : TypeToken<MutableList<WishItem>>() {}.type
        return gson.fromJson(json, type) ?: mutableListOf()
    }

    private fun saveFavorites(favorites: List<WishItem>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString("favorite_items", json)
        editor.apply()
    }

    private fun isFavoriteItem(item: WishItem): Boolean {
        return loadFavorites().any { it.title == item.title }
    }

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
