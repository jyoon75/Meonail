package com.example.meonail

import RecordDatabaseHelper
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.adapter.RecordAdapter
import com.example.meonail.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Calendar

private const val ARG_URI = "uri"

class CalendarFragment : Fragment(), CalenderAdapter.ItemClickListener {
    private var uri: String? = null

    private lateinit var txtvMonth: TextView
    private lateinit var btnBeforeMonth: ImageButton
    private lateinit var btnAfterMonth: ImageButton

    private lateinit var calendarAdapter: CalenderAdapter
    private lateinit var weekListAdapter: WeekAdapter

    private lateinit var dayList: ArrayList<CalenderDay>
    private lateinit var dayOfWeekList: ArrayList<String>

    private lateinit var rvCalendar: RecyclerView
    private lateinit var rvWeeklist: RecyclerView

    private lateinit var calendar: Calendar

    private val YEAR = "year"
    private val MONTH = "month"
    private val DATE = "date"

    private lateinit var databaseHelper: RecordDatabaseHelper

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

        txtvMonth = view.findViewById(R.id.txtvMonth)
        btnBeforeMonth = view.findViewById(R.id.btnBeforeMonth)
        btnAfterMonth = view.findViewById(R.id.btnAfterMonth)

        btnBeforeMonth.setOnClickListener{
            calendar.add(Calendar.MONTH, -1)
            setCalendarView(calendar)
        }

        btnAfterMonth.setOnClickListener{
            calendar.add(Calendar.MONTH, 1)
            setCalendarView(calendar)
        }

        // 7개의 열을 설정하는GridLayoutManager 사용
        rvCalendar = view.findViewById(R.id.rvCalendar)
        rvCalendar.layoutManager = GridLayoutManager(requireContext(), 7)
        rvCalendar.setItemViewCacheSize(42)

        rvWeeklist = view.findViewById(R.id.rvWeeklist)
        rvWeeklist.layoutManager = GridLayoutManager(requireContext(), 7)
        rvWeeklist.setItemViewCacheSize(7)

        databaseHelper = RecordDatabaseHelper(requireContext())

        return view
    }

    //뷰가 생성된 직후에 호출됨
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setWeeklistbar() // 요일 바 설정
        setCalendarView(getCalendar(savedInstanceState))
    }

    // 캘린더 인스턴스 생성 또는 불러오기
    private fun getCalendar(savedInstanceState: Bundle?): Calendar {
        calendar = Calendar.getInstance()
        savedInstanceState?.let {
            calendar.set(it.getInt(YEAR), it.getInt(MONTH), 1)
        } ?: run {
            calendar.set(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar
    }


    // 상태 저장
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(YEAR, calendar.get(Calendar.YEAR))
        outState.putInt(MONTH, calendar.get(Calendar.MONTH))
        outState.putInt(DATE, calendar.get(Calendar.DAY_OF_MONTH))
    }

    override fun onResume() {
        super.onResume()
        setCalendarView(calendar)
    }

    // 요일 바 설정
    private fun setWeeklistbar() {
        dayOfWeekList = arrayListOf("일", "월", "화", "수", "목", "금", "토")
        weekListAdapter = WeekAdapter(requireContext(), dayOfWeekList)
        rvWeeklist.adapter = weekListAdapter
    }

    // 달력 세팅하여 어댑터에 전달
    private fun setCalendarView(calendar: Calendar) {
        dayList = ArrayList()

        var lastMonthStartDay: Int //이전 달의 마지막 날짜
        val dayOfMonth = calendar.get(Calendar.DAY_OF_WEEK) //1일의 요일 ( 1(일) ~ 7(토) )
        val thisMonthLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //현재 월의 마지막 날짜

        calendar.add(Calendar.MONTH, -1) //월 1 감소(이전 달로)
        lastMonthStartDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) //이전 달의 마지막 날 저장
        calendar.add(Calendar.MONTH, 1) //월 1 증가(현재 달로)

        lastMonthStartDay -= (dayOfMonth - 1) - 1

        //월 설정
        txtvMonth.text = calendar.get(Calendar.YEAR).toString() + " " + (calendar.get(Calendar.MONTH) + 1).toString() + "월"

        // 이전 달의 날짜 설정
        for (i in 0 until dayOfMonth - 1) {
            val date = lastMonthStartDay + i
            val day = CalenderDay(day = date, inMonth = false, image = null)
            dayList.add(day)
        }

        // 이번 달의 날짜 설정
        for (i in 1..thisMonthLastDay) {
            // 해당 일에 데이터 있다면 첫 번째 이미지를 이미지로 하며 캘린더데이 생성
            val itemCal = calendar.clone() as Calendar
            itemCal.set(Calendar.DAY_OF_MONTH, i)
            val date = SimpleDateFormat("yyyy.MM.dd").format(itemCal.time)
            val imageUris = databaseHelper.getImageUrisForDate(date)
            var imageUri: String? = null
            if (imageUris.isNotEmpty()){
                imageUri = imageUris[0]  // 첫 번째 이미지 사용
            }

            val day = CalenderDay(day = i, inMonth = true, image = imageUri)
            dayList.add(day)
        }

        // 6줄 중 남은 칸 설정
        for (i in 1..(42 - thisMonthLastDay - dayOfMonth + 1)) {
            val day = CalenderDay(day = i, inMonth = false, image = null)
            dayList.add(day)
        }

        initCalendarAdapter() // 캘린더 어댑터 초기화 및 뷰에 표시
    }

    // 캘린더뷰 어댑터 초기화 및 뷰에 표시
    private fun initCalendarAdapter() {
        calendarAdapter = CalenderAdapter(requireContext(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), dayList)
        calendarAdapter.setClickListener(this)
        rvCalendar.adapter = calendarAdapter
    }

    // 캘린더뷰 아이템 클릭 리스너
    override fun onItemClick(view: View?, day: String, isInMonth: Boolean) {
        val itemCal = calendar.clone() as Calendar
        itemCal.set(Calendar.DAY_OF_MONTH, day.toInt())
        val date = SimpleDateFormat("yyyy.MM.dd").format(itemCal.time)
        val imageUris = databaseHelper.getImageUrisForDate(date)

        if (isInMonth && imageUris.isNotEmpty()) {
            showRecordInfoForDate(date) // 날짜로 아이디 얻어 상세 정보 페이지 열기
        }
    }

    // 날짜로 아이디 얻어 상세 정보 페이지 열기
    private fun showRecordInfoForDate(date: String) {
        // 주어진 날짜에 해당하는 첫 번째 기록의 아이디
        val records = databaseHelper.getAllRecords("ASC") // 정렬 순서 선택
        val record = records.firstOrNull { it.getAsString(RecordDatabaseHelper.COLUMN_DATE) == date }
        val recordId = record?.getAsInteger(RecordDatabaseHelper.COLUMN_ID)

        // 기록 아이디로 상세 정보 프래그먼트 열기
        val fragment = RecordInfoFragment().apply {
            arguments = Bundle().apply {
                putInt("record_id", recordId ?: -1) // recordId가 null이면 -1로 처리
            }
        }
        findNavController().navigate(R.id.action_calendar_to_recordInfo, fragment.arguments)
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