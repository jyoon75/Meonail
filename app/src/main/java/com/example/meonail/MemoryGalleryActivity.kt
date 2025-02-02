package com.example.meonail

import RecordDatabaseHelper
import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.meonail.R
import com.example.meonail.databinding.ActivityMemoryGalleryBinding
import java.util.Random

class MemoryGalleryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMemoryGalleryBinding
    private lateinit var databaseHelper: RecordDatabaseHelper
    private lateinit var records: List<ContentValues>

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_memory_gallery)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMemoryGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = RecordDatabaseHelper(this)

        // 안내 화면
        binding.hintSwipeGallery.visibility = FrameLayout.VISIBLE
        binding.hintSwipeGallery.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_MOVE) {
                binding.hintSwipeGallery.visibility = FrameLayout.GONE // 터치 시 안 보이게 함
            }
            true
        }

        // Intent에서 type 값 가져오기
        val type = intent.getStringExtra("type") ?: ""

        // 해당 type에 맞는 레코드를 가져오기
        if (type == "All") {
            records = databaseHelper.getAllRecords()
        }
        else if (type == "RatingFive") {
            records = databaseHelper.getAllRecordsWithRatingFive()
        }
        else if (type == "LongNote") {
            records = databaseHelper.getRecordsWithNoteLengthGreaterThan()
            //Log.e("AAAAAAA", databaseHelper.getTest("4").length.toString())
        }

        // records가 비어 있으면, 사용자에게 알리고 종료
        if (records.isEmpty()) {
            Toast.makeText(this, "해당되는 기록이 없습니다.", Toast.LENGTH_SHORT).show()
            finish() // 액티비티 종료
            return // 더 이상 진행하지 않음
        }

        // 랜덤으로 섞기
        records = records.shuffled(Random(System.currentTimeMillis()))

        // ViewPager2와 Adapter 설정
        val adapter = MemoryAdapter(this, records)
        binding.viewPagerMemory.adapter = adapter
    }
}