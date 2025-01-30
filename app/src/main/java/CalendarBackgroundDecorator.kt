package com.example.meonail

import RecordDatabaseHelper
import android.content.ContentResolver
import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarBackgroundDecorator(private val context: Context, private val databaseHelper: RecordDatabaseHelper) : DayViewDecorator {
    private val backgroundDrawable: Drawable = context.getDrawable(R.drawable.ic_launcher_background)!!
    private var targetDay: CalendarDay? = null // 선택된 날짜를 저장할 변수

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        targetDay = day
        day?.let {
            // 데이터베이스에서 날짜에 해당하는 이미지 URI를 가져옴
            val date = "${day.year}.${String.format("%02d", day.month+1)}.${String.format("%02d", day.day)}"
            val imageUris = databaseHelper.getImageUrisForDate(date)

            // URI가 있으면 배경을 설정
            return imageUris.isNotEmpty()
        }
        return false
    }

    override fun decorate(view: DayViewFacade?) {
        view?.setBackgroundDrawable(backgroundDrawable)
        // 데이터베이스에서 해당 날짜에 대한 이미지 URI 목록 가져오기
    }
}