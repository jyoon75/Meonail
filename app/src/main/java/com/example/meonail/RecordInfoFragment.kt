package com.example.meonail

import RecordDatabaseHelper
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class RecordInfoFragment : Fragment(R.layout.fragment_record_info) {

    private var recordId: Int? = null
    private lateinit var databaseHelper: RecordDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordId = arguments?.getInt("record_id")
        databaseHelper = RecordDatabaseHelper(requireContext())

        setHasOptionsMenu(true) // 뒤로가기 버튼을 활성화
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 상단 앱바에 뒤로가기 화살표 표시
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼 표시
            title = "기록 상세보기" // 원하는 제목 설정
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record_info, container, false)

        // UI 요소 초기화
        val imageContainer = view.findViewById<LinearLayout>(R.id.imageContainer)
        val textViewTitleInfo = view.findViewById<TextView>(R.id.textViewTitleInfo)
        val ratingBarInfo = view.findViewById<RatingBar>(R.id.ratingBarInfo)
        val textViewTagsInfo = view.findViewById<TextView>(R.id.textViewTagsInfo)
        val textViewNoteInfo = view.findViewById<TextView>(R.id.textViewNoteInfo)
        val textViewDateInfo = view.findViewById<TextView>(R.id.textViewDateInfo)

        /*// 데이터베이스에서 기록 정보 불러오기
        recordId?.let {
            val record = databaseHelper.getRecordById(it)
            record?.let { data ->
                // 제목
                textViewTitleInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_TITLE)

                // 별점
                ratingBarInfo.rating = data.getAsFloat(RecordDatabaseHelper.COLUMN_RATING)

                // 태그
                val tags = data.getAsString(RecordDatabaseHelper.COLUMN_TAGS)
                // 태그가 비어 있으면 #을 표시하지 않도록
                if (tags.isNotEmpty()) {
                    textViewTagsInfo.text = tags.split(",")
                        .filter { it.isNotBlank() }  // 공백 태그 제거
                        .joinToString(" ") { "#$it" }
                } else {
                    textViewTagsInfo.text = "" // 태그가 없으면 공백 처리
                }

                // 기록 내용
                textViewNoteInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_NOTE)

                /*// 이미지 설정
                val imageUris = data.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",")
                if (!imageUris.isNullOrEmpty()) {
                    imageContainer.setImageURI(Uri.parse(imageUris[0]))
                }*/
                /*// 이미지 설정
                val imageUris = data.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",")
                if (!imageUris.isNullOrEmpty()) {
                    imageUris.forEach { uri ->
                        val imageView = ImageView(requireContext()).apply {
                            *//*layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                                setMargins(8, 0, 8, 0) // 이미지 간격
                            }
                            setImageURI(Uri.parse(uri))
                            scaleType = ImageView.ScaleType.CENTER_CROP*//*
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,  // 가로 크기: 원래 이미지 크기
                                LinearLayout.LayoutParams.WRAP_CONTENT   // 세로 크기: 원래 이미지 크기
                            ).apply {
                                setMargins(8, 0, 8, 0) // 이미지 간격
                            }
                            setImageURI(Uri.parse(uri)) // URI를 사용해 이미지 설정
                            scaleType = ImageView.ScaleType.FIT_CENTER // 이미지를 중앙에 맞춤 (필요 시 제거 가능)
                            adjustViewBounds = true // 이미지가 원래 비율로 유지되도록 설정
                        }
                        imageContainer.addView(imageView) // 이미지 추가
                    }
                }*/

                // 기록 날짜
                val recordDate = data.getAsString(RecordDatabaseHelper.COLUMN_DATE) // 날짜 가져오기
                textViewDateInfo.text = "기록일: $recordDate"
            }
        }*/

        return view
    }


    // 수정 후 기록 불러오기
    override fun onResume() {
        super.onResume()
        loadRecordData()
    }

    // 수정 후 기록 불러오기 로딩 근데 이미지는 빠져있음
    private fun loadRecordData() {
        view?.let { view ->
            val imageContainer = view.findViewById<LinearLayout>(R.id.imageContainer)
            val textViewTitleInfo = view.findViewById<TextView>(R.id.textViewTitleInfo)
            val ratingBarInfo = view.findViewById<RatingBar>(R.id.ratingBarInfo)
            val textViewTagsInfo = view.findViewById<TextView>(R.id.textViewTagsInfo)
            val textViewNoteInfo = view.findViewById<TextView>(R.id.textViewNoteInfo)
            val textViewDateInfo = view.findViewById<TextView>(R.id.textViewDateInfo)

            recordId?.let {
                val record = databaseHelper.getRecordById(it)
                record?.let { data ->
                    textViewTitleInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_TITLE)
                    ratingBarInfo.rating = data.getAsFloat(RecordDatabaseHelper.COLUMN_RATING)
                    textViewTagsInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_TAGS)
                        .split(",")
                        .filter { it.isNotBlank() }
                        .joinToString(" ") { "#$it" }
                    textViewNoteInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_NOTE)
                    textViewDateInfo.text = "기록일: ${data.getAsString(RecordDatabaseHelper.COLUMN_DATE)}"


                    /*// 이미지 로드
                    val imageUris = data.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",") ?: listOf()*/
                    // 이미지 썸네일 설정 (Glide 사용)
                    val imageUris = record.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",") ?: listOf()

                    // 기존 이미지 제거 후 추가
                    imageContainer.removeAllViews()

                    if (imageUris.isNotEmpty() && imageUris[0].isNotBlank()) {
                        val imagePath = imageUris[0].trim()
                        val uri = Uri.parse(imagePath)

                        imageUris.forEach { uriString ->
                            val imageView = ImageView(requireContext()).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    400, // 가로 크기
                                    400  // 세로 크기
                                ).apply {
                                    setMargins(16, 8, 16, 8) // 이미지 간격 추가
                                }
                                scaleType = ImageView.ScaleType.CENTER_CROP
                            }

                            // Glide를 사용하여 이미지 로드
                            Glide.with(requireContext())
                                .load(uri) // content:// URI 직접 로드
                                .diskCacheStrategy(DiskCacheStrategy.ALL) // 캐싱 전략
                                .placeholder(R.mipmap.ic_launcher) // 로딩 중 이미지
                                .error(R.drawable.ic_dashboard_black_24dp) // 실패 시 기본 이미지
                                .into(imageView)

                            imageContainer.addView(imageView) // 컨테이너에 이미지 추가
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        // 뒤로가기 버튼 비활성화 (필요 시)
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 뒤로가기 버튼 클릭 시
                requireActivity().onBackPressed() // 이전 화면으로 이동
                true
            }
            R.id.menu_edit -> {
                // 메뉴 기록 수정
                editRecord()
                true
            }
            R.id.menu_delete -> {
                // 메뉴 기록 삭제
                deleteRecord()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    // 수정, 삭제 메뉴
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_record_info, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun editRecord() {
        val intent = Intent(requireContext(), RecordRegistActivity::class.java).apply {
            putExtra("record_id", recordId)
        }
        startActivity(intent)
    }

    private fun deleteRecord() {
        AlertDialog.Builder(requireContext())
            .setTitle("삭제 확인")
            .setMessage("정말 삭제하시겠습니까?")
            .setPositiveButton("삭제") { _, _ ->
                recordId?.let {
                    databaseHelper.deleteRecord(it)
                    Toast.makeText(requireContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                    requireActivity().onBackPressed()
                }
            }
            .setNegativeButton("취소", null)
            .show()
    }






    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}
