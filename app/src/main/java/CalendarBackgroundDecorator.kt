package com.example.meonail

import android.content.Context
import android.graphics.drawable.Drawable
import android.icu.util.Calendar
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

class CalendarBackgroundDecorator(context: Context) : DayViewDecorator {
    private val backgroundDrawable: Drawable = context.getDrawable(R.drawable.ic_launcher_background)!!

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // 특정 날짜에 이미지를 적용할지 결정하는 조건
        // 예를 들어, 특정 날짜가 오늘이라면
        //return day!!.date == Calendar.getInstance().time
        return true
    }

    override fun decorate(view: DayViewFacade?) {
        //val context = view.context
        // 여기서 날짜에 해당하는 이미지를 설정
        //view!!.setBackgroundDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_launcher_background))
        view?.setBackgroundDrawable(backgroundDrawable)
    }
}