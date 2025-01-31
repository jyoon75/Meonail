package com.example.meonail

import HomeViewModel
import RecordDatabaseHelper
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class RecordRegistActivity : AppCompatActivity() {

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var dbHelper: RecordDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_regist)

        // 날짜 선택
        val recordEditTextDate = findViewById<EditText>(R.id.editTextDate)
        recordEditTextDate.setOnClickListener {
            showDatePickerDialog(recordEditTextDate)
        }


        // 태그 추가
        val addTagButton = findViewById<Button>(R.id.btnRecordAddTag)
        val recordTagContainer = findViewById<LinearLayout>(R.id.tagContainer)
        addTagButton.setOnClickListener {
            showAddTagDialog(recordTagContainer)
        }


        // 이미지 추가
        val recordImageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        val addImageButton = findViewById<Button>(R.id.btnAddImage)
        addImageButton.setOnClickListener {
            openGallery()
        }

        // 이미지 추가 결과 처리
        galleryResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data: Intent? = result.data
                val clipData = data?.clipData
                val selectedImageUri: Uri? = data?.data

                if (clipData != null) {
                    // 여러 개 선택된 경우
                    for (i in 0 until clipData.itemCount) {
                        val uri = clipData.getItemAt(i).uri
                        addImageToContainer(recordImageContainer, uri)
                    }
                } else if (selectedImageUri != null) {
                    // 한 개만 선택된 경우
                    addImageToContainer(recordImageContainer, selectedImageUri)
                }
            }
        }



        // DB 저장
        dbHelper = RecordDatabaseHelper(this)

        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val editTextTitle = findViewById<EditText>(R.id.editTextTitle)
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        val ratingBar = findViewById<RatingBar>(R.id.ratingBar)
        val tagContainer = findViewById<LinearLayout>(R.id.tagContainer)
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
        val editTextNote = findViewById<EditText>(R.id.editTextNote)
        val checkBoxPrivate = findViewById<CheckBox>(R.id.checkBoxPrivate)
        val saveButton = findViewById<Button>(R.id.btnRecordSave)



        // ✅ 기존 기록을 불러오는 코드 추가 (수정 모드일 경우)
        if (intent.hasExtra("record_id")) {
            val recordId = intent.getIntExtra("record_id", -1)
            val record = dbHelper.getRecordById(recordId)
            if (record != null) {
                editTextTitle.setText(record.getAsString(RecordDatabaseHelper.COLUMN_TITLE))
                editTextDate.setText(record.getAsString(RecordDatabaseHelper.COLUMN_DATE))
                ratingBar.rating = record.getAsFloat(RecordDatabaseHelper.COLUMN_RATING)
                editTextNote.setText(record.getAsString(RecordDatabaseHelper.COLUMN_NOTE))
                checkBoxPrivate.isChecked = record.getAsInteger(RecordDatabaseHelper.COLUMN_PRIVATE) == 1
            }
        }


        saveButton.setOnClickListener {
            saveRecordToDatabase(
                spinnerCategory.selectedItem.toString(),
                editTextTitle.text.toString().trim(),
                editTextDate.text.toString().trim(),
                ratingBar.rating,
                getTagsFromContainer(tagContainer),
                getImagesFromContainer(imageContainer),
                editTextNote.text.toString().trim(),
                checkBoxPrivate.isChecked
            )
        }

    }

    // 날짜 선택하는 창
    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            // 선택된 날짜를 EditText에 표시
            val formattedDate = String.format("%04d.%02d.%02d", selectedYear, selectedMonth + 1, selectedDay)
            editText.setText(formattedDate)
        }, year, month, day)

        datePickerDialog.show()
    }


    // 태그 추가
    private fun showAddTagDialog(container: LinearLayout) {
        // 다이얼로그 내부에 입력 필드 생성
        val inputField = EditText(this).apply {
            hint = "태그 입력"
            setPadding(16, 8, 16, 8)
        }

        // AlertDialog 생성
        AlertDialog.Builder(this)
            .setTitle("태그 추가")
            .setView(inputField)
            .setPositiveButton("추가") { dialog, _ ->
                val tagText = inputField.text.toString().trim()
                if (tagText.isNotEmpty()) {
                    addTagToContainer(container, tagText)
                } else {
                    Toast.makeText(this, "태그가 입력되지 않았습니다.", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("취소") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    private fun addTagToContainer(container: LinearLayout, tagText: String) {
        // 태그 뷰 생성
        val tagView = TextView(this).apply {
            text = "#$tagText"
            setPadding(16, 8, 16, 8)
            //setBackgroundResource(R.drawable.tag_background) // 태그 배경
            setTextColor(ContextCompat.getColor(context, R.color.primary_ver2))
            setOnClickListener {
                container.removeView(this) // 클릭 시 태그 삭제
            }
        }
        container.addView(tagView)
    }


    // 이미지 추가
    // 갤러리 여는 메서드
    private lateinit var galleryResultLauncher: androidx.activity.result.ActivityResultLauncher<Intent>
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) // 다중 선택 허용
        }
        galleryResultLauncher.launch(intent)
    }

    // 선택한 이미지를 컨테이너에 추가하는 메서드
    private fun addImageToContainer(container: LinearLayout, imageUri: Uri) {
        val imageLayout = FrameLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(200, 200).apply {
                setMargins(8, 8, 8, 8)
            }
        }

        // 이미지 뷰
        val imageView = ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            setImageURI(imageUri)
            scaleType = ImageView.ScaleType.CENTER_CROP
            tag = imageUri.toString() // URI를 태그로 설정
        }

        // 삭제 버튼
        val deleteButton = ImageButton(this).apply {
            layoutParams = FrameLayout.LayoutParams(50, 50).apply {
                marginEnd = 8
                topMargin = 8
            }
            setImageResource(android.R.drawable.ic_menu_close_clear_cancel)
            background = null
            setOnClickListener {
                container.removeView(imageLayout) // 삭제 버튼 클릭 시 이미지 삭제
            }
        }

        // 이미지와 삭제 버튼을 프레임 레이아웃에 추가
        imageLayout.addView(imageView)
        imageLayout.addView(deleteButton)
        container.addView(imageLayout)
    }



    // DB 저장
    private fun saveRecordToDatabase(
        category: String,
        title: String,
        date: String,
        rating: Float,
        tags: String,
        images: String,
        note: String,
        isPrivate: Boolean
    ) {
        if (title.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "제목과 날짜는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show()
            return
        }

        val values = ContentValues().apply {
            put(RecordDatabaseHelper.COLUMN_CATEGORY, category)
            put(RecordDatabaseHelper.COLUMN_TITLE, title)
            put(RecordDatabaseHelper.COLUMN_DATE, date)
            put(RecordDatabaseHelper.COLUMN_RATING, rating)
            put(RecordDatabaseHelper.COLUMN_TAGS, tags)
            put(RecordDatabaseHelper.COLUMN_IMAGES, images)
            put(RecordDatabaseHelper.COLUMN_NOTE, note)
            put(RecordDatabaseHelper.COLUMN_PRIVATE, if (isPrivate) 1 else 0)
        }


        // 기존 레코드 수정 모드일 때
        val recordId = intent.getIntExtra("record_id", -1)
        if (recordId != -1) {
            // 기존 레코드를 업데이트
            val rowsAffected = dbHelper.updateRecord(values, recordId)
            if (rowsAffected > 0) {
                Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show()

                // 기록을 최신순으로 로드
                homeViewModel.loadRecords(dbHelper, "DESC")

                val intent = Intent().apply {
                    putExtra("updated_record_id", recordId) // 수정된 레코드의 ID 전달
                }
                setResult(Activity.RESULT_OK, intent)
                finish() // 액티비티 종료
            } else {
                Toast.makeText(this, "수정에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 새로 추가하는 경우
            val id = dbHelper.insertRecord(values)
            if (id > 0) {
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()

                homeViewModel.loadRecords(dbHelper, "DESC")

                val intent = Intent().apply {
                    putExtra("new_record_id", id)
                }
                setResult(Activity.RESULT_OK, intent)
                clearInputs() // 입력 칸 초기화
                finish() // 액티비티 종료
            } else {
                Toast.makeText(this, "저장에 실패했습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun getTagsFromContainer(container: LinearLayout): String {
        val tags = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val tagView = container.getChildAt(i) as? TextView
            tagView?.text?.let {
                tags.add(it.toString().removePrefix("#"))
            }
        }
        return tags.joinToString(",")
    }


    private fun getImagesFromContainer(container: LinearLayout): String {
        val imageUris = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val frameLayout = container.getChildAt(i) as? FrameLayout
            val imageView = frameLayout?.getChildAt(0) as? ImageView
            imageView?.tag?.let { uri ->
                imageUris.add(uri.toString()) // 태그에서 URI 추출
            }
        }
        return imageUris.joinToString(",") // 쉼표로 구분된 URI 문자열 반환
    }

    private fun clearInputs() {
        findViewById<EditText>(R.id.editTextTitle).text.clear()
        findViewById<EditText>(R.id.editTextDate).text.clear()
        findViewById<RatingBar>(R.id.ratingBar).rating = 0f
        findViewById<LinearLayout>(R.id.tagContainer).removeAllViews()
        findViewById<LinearLayout>(R.id.imageContainer).removeAllViews()
        findViewById<EditText>(R.id.editTextNote).text.clear()
        findViewById<CheckBox>(R.id.checkBoxPrivate).isChecked = false
    }

}
