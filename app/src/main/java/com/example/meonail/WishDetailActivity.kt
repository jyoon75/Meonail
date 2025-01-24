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
    private lateinit var txtType: TextView
    private lateinit var txtPeriod: TextView
    private lateinit var txtLocation: TextView
    private lateinit var txtContact: TextView
    private lateinit var btnCreateImage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wish_detail)

        // ✅ 액션바에 뒤로 가기 버튼 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        imgPoster = findViewById(R.id.imgWishPoster)
        txtTitle = findViewById(R.id.txtWishTitle)
        txtType = findViewById(R.id.txtWishType)
        txtPeriod = findViewById(R.id.txtWishPeriod)
        txtLocation = findViewById(R.id.txtWishLocation)
        txtContact = findViewById(R.id.txtWishContact)
        btnCreateImage = findViewById(R.id.btnCreateImage)

        // ✅ Intent로 전달된 WishItem 데이터 가져오기
        val wishItem: WishItem? = intent.getParcelableExtra("wishItem")

        wishItem?.let { item ->
            txtTitle.text = item.title
            txtPeriod.text = "기간: ${item.period}" // ✅ 전시 또는 행사 기간
            txtLocation.text = "장소: ${item.eventSite}" // ✅ 행사 장소
            txtContact.text = "문의: ${item.contactPoint}" // ✅ 문의 연락처

            Glide.with(this)
                .load(item.imageObject)
                .into(imgPoster)

            // ✅ 🔥 버튼 클릭 시 이미지 생성 화면으로 이동
            btnCreateImage.setOnClickListener {
                val intent = Intent(this, ImageCreationActivity::class.java).apply {
                    putExtra("wishItem", item)
                }
                startActivity(intent)
            }
        }
    }
    // ✅ 뒤로 가기 버튼 클릭 시 실행
    override fun onSupportNavigateUp(): Boolean {
        finish() // 현재 액티비티 종료
        return true
    }
}
