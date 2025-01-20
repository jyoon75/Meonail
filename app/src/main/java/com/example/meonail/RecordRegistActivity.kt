package com.example.meonail

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RecordRegistActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_regist)

        val editTextDate = findViewById<EditText>(R.id.editTextDate)

        // 날짜 선택 이벤트
        editTextDate.setOnClickListener {
            showDatePickerDialog(editTextDate)
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
}
