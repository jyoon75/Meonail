package com.example.meonail

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import androidx.core.content.FileProvider
import com.example.meonail.model.WishItem

class ImageCreationActivity : AppCompatActivity() {

    private lateinit var imgPoster: ImageView
    private lateinit var editText: EditText
    private lateinit var btnGenerateImage: Button
    private lateinit var btnSave: Button
    private var generatedBitmap: Bitmap? = null // 생성된 이미지를 저장할 변수

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_creation)

        // ✅ 액션바에 뒤로 가기 버튼 추가
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "이미지 생성하기"

        // 🔥 API 33 이상 (Android 13 이상)에서도 정상 동작하도록 설정
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        imgPoster = findViewById(R.id.imgGeneratedImage)
        editText = findViewById(R.id.editTextOverlay)
        btnGenerateImage = findViewById(R.id.btnGenerateImage)
        btnSave = findViewById(R.id.btnSave)

        btnGenerateImage.visibility = View.VISIBLE
        btnSave.visibility = View.GONE
        editText.visibility = View.VISIBLE

        val wishItem = intent.getParcelableExtra<WishItem>("wishItem")

        wishItem?.let {
            Glide.with(this)
                .load(it.imageObject) // 🔥 여기서 `imageUrl`이 아니라 `url` 사용
                .into(imgPoster)
        }

        // 🎨 이미지 생성 버튼 클릭 시
        btnGenerateImage.setOnClickListener {
            val textOverlay = editText.text.toString() // 🔥 사용자가 입력한 텍스트 가져오기
            generatedBitmap = createCustomImage(imgPoster, textOverlay) // ✅ 폴라로이드 스타일 적용
            imgPoster.setImageBitmap(generatedBitmap) // ✅ 이미지 업데이트
            btnGenerateImage.visibility = View.GONE
            btnSave.visibility = View.VISIBLE
            editText.visibility = View.GONE
        }

        // 💾 저장 버튼 클릭 시
        btnSave.setOnClickListener {
            generatedBitmap?.let {
                val uri = saveImageToGallery(it)
                uri?.let {
                    Toast.makeText(this, "이미지 저장 완료!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // 🎨 이미지 생성 함수
    private fun createCustomImage(imageView: ImageView, overlayText: String): Bitmap {
        val drawable = imageView.drawable ?: return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)

        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight

        // ✅ 폴라로이드 스타일 여백 설정
        val borderSize = 50  // 상하좌우 여백
        val extraBottomSpace = 200  // 하단 여백 (텍스트 영역)

        val finalWidth = originalWidth + (borderSize * 2)
        val finalHeight = originalHeight + (borderSize * 2) + extraBottomSpace

        val finalBitmap = Bitmap.createBitmap(finalWidth, finalHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(finalBitmap)

        // ✅ 배경을 흰색으로 설정 (폴라로이드 효과)
        val paint = Paint().apply {
            color = Color.WHITE
            style = Paint.Style.FILL
        }
        canvas.drawRect(0f, 0f, finalWidth.toFloat(), finalHeight.toFloat(), paint)

        // ✅ 포스터 이미지를 중앙 정렬하여 추가
        val posterLeft = borderSize
        val posterTop = borderSize
        val posterRight = posterLeft + originalWidth
        val posterBottom = posterTop + originalHeight

        drawable.setBounds(posterLeft, posterTop, posterRight, posterBottom)
        drawable.draw(canvas)

        // ✅ 텍스트 추가 (하단 여백 중앙 정렬)
        paint.apply {
            color = Color.BLACK
            textSize = 50f
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val textX = finalWidth / 2f
        val textY = finalHeight - (extraBottomSpace / 2f) + 20 // 하단 여백 내 중앙 정렬
        canvas.drawText(overlayText, textX, textY, paint)

        return finalBitmap
    }

    // 💾 이미지 저장 함수 (갤러리에 자동 반영)
    private fun saveImageToGallery(bitmap: Bitmap): Uri? {
        val filename = "polaroid_${System.currentTimeMillis()}.png"
        val directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, filename)
        return try {
            FileOutputStream(file).use {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
            }

            // ✅ 갤러리에 강제 반영
            MediaScannerConnection.scanFile(
                this,
                arrayOf(file.absolutePath),
                arrayOf("image/png")
            ) { _, _ -> }

            Uri.fromFile(file) // 저장한 이미지의 URI 반환
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "이미지 저장 실패", Toast.LENGTH_SHORT).show()
            null
        }
    }
    // ✅ 액션바 뒤로 가기 버튼 클릭 시 처리
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    // ✅ 기본 뒤로 가기 버튼 처리
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
