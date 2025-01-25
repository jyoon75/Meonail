package com.example.meonail

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.adapter.WishListAdapter
import com.example.meonail.model.WishItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WishListActivity : AppCompatActivity() {

    private lateinit var rvWishList: RecyclerView
    private lateinit var wishListAdapter: WishListAdapter
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        rvWishList = findViewById(R.id.rvWishList)
        emptyTextView = findViewById(R.id.txtEmptyWishList)

        wishListAdapter = WishListAdapter(this, isWishList = true) // 🔥 콜백 함수 제거

        rvWishList.layoutManager = LinearLayoutManager(this)
        rvWishList.adapter = wishListAdapter

        loadWishList()
    }

    // ✅ 🔥 뒤로 가기 버튼이 눌렸을 때 실행
    override fun onSupportNavigateUp(): Boolean {
        finish() // 현재 액티비티 종료
        return true
    }

    private fun loadWishList() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("WishPrefs", MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("favorite_items", null)
        val type = object : TypeToken<List<WishItem>>() {}.type

        val wishList: List<WishItem> = gson.fromJson(json, type) ?: emptyList()

        if (wishList.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE // ✅ "아무것도 없습니다" 메시지 표시
            rvWishList.visibility = View.GONE // ✅ RecyclerView 숨김
        } else {
            emptyTextView.visibility = View.GONE // ✅ 메시지 숨김
            rvWishList.visibility = View.VISIBLE // ✅ RecyclerView 보이기
            wishListAdapter.updateData(wishList)
        }
    }
}
