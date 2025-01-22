package com.example.meonail

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.*

class RecordRegistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_regist)

        // 날짜 선택
        val editTextDate = findViewById<EditText>(R.id.editTextDate)
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
        }


        // 태그 추가
        val addTagButton = findViewById<Button>(R.id.btnRecordAddTag)
        val tagContainer = findViewById<LinearLayout>(R.id.tagContainer)
        addTagButton.setOnClickListener {
            showAddTagDialog(tagContainer)
        }


        // 이미지 추가
        val imageContainer = findViewById<LinearLayout>(R.id.imageContainer)
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
                        addImageToContainer(imageContainer, uri)
                    }
                } else if (selectedImageUri != null) {
                    // 한 개만 선택된 경우
                    addImageToContainer(imageContainer, selectedImageUri)
                }
            }
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

}
