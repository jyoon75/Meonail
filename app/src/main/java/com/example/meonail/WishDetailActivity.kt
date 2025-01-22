package com.example.meonail

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.meonail.model.WishItem

class WishDetailActivity : AppCompatActivity() {

    private lateinit var imgPoster: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtFee: TextView
    private lateinit var btnCreateImage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_detail) // ✅ 새로운 레이아웃 설정

        // ✅ 액션바에 뒤로 가기 버튼 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgPoster = findViewById(R.id.imgWishPoster)
        txtTitle = findViewById(R.id.txtWishTitle)
        txtCategory = findViewById(R.id.txtWishCategory)
        txtDate = findViewById(R.id.txtWishDate)
        txtFee = findViewById(R.id.txtWishFee)
        btnCreateImage = findViewById(R.id.btnCreateImage)

        // ✅ Intent로 전달된 WishItem 데이터 가져오기
        val wishItem: WishItem? = intent.getParcelableExtra("wishItem")

        wishItem?.let { item ->
            txtTitle.text = item.title
            txtCategory.text = "카테고리: ${item.category}"
            txtDate.text = "일정: ${item.eventTime ?: "날짜 정보 없음"}"
            txtFee.text = "참가비: ${item.participationFee ?: "무료"}"

            Glide.with(this).load(item.imageUrl).into(imgPoster)

        }
    }

    // ✅ 뒤로 가기 버튼 클릭 시 실행
    override fun onSupportNavigateUp(): Boolean {
        finish() // 현재 액티비티 종료
        return true
    }
}
