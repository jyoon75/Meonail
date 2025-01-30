package com.example.meonail

import RecordDatabaseHelper
import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.adapter.RecordAdapter
import com.example.meonail.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

private const val ARG_URI = "uri"

class CalendarFragment : Fragment() {
    private var uri: String? = null

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var databaseHelper: RecordDatabaseHelper

    //private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            // 파라미터 초기화
            uri = it.getString(ARG_URI)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        databaseHelper = RecordDatabaseHelper(requireContext())

        setupCalendarView()
        return view
    }

    //뷰가 생성된 직후에 호출됨
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 날짜 클릭 이벤트 처리
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 해당 날짜에 맞는 상세 정보 표시
                showDetailsForDate(date)
            }
        }
    }

    private fun setupCalendarView() {
        // CustomDayDecorator를 calendarView에 적용
        calendarView.addDecorator(CalendarBackgroundDecorator(requireContext(), databaseHelper))
    }

    private fun showDetailsForDate(date: CalendarDay) {
        // 해당 날짜에 맞는 상세 정보를 보여주는 코드 작성(화면 이동 예정)
        Toast.makeText(context, "상세 정보: ${date.year}.${String.format("%02d", date.month+1)}.${String.format("%02d", date.day)}", Toast.LENGTH_SHORT).show()
    }


    companion object {
        // 파라미터 정의
        @JvmStatic
        fun newInstance(uri: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_URI, uri)
                }
            }
    }
}