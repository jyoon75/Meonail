package com.example.meonail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.databinding.FragmentCalendarBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CalendarFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // 날짜 목록을 저장할 ArrayList
    //private val dates = ArrayList<CalenderDay>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendarView = view.findViewById<MaterialCalendarView>(R.id.calendarView)

        // 각 날짜마다 다른 이미지 설정하기
        val decorator = CalendarBackgroundDecorator(requireContext()) // 현재 프래그먼트의 컨텍스트 가져옴
        calendarView.addDecorator(decorator)

        // 날짜 클릭 이벤트 처리
        calendarView.setOnDateChangedListener { widget, date, selected ->
            if (selected) {
                // 해당 날짜에 맞는 상세 정보 표시
                showDetailsForDate(date)
            }
        }
    }

    private fun showDetailsForDate(date: CalendarDay) {
        // 해당 날짜에 맞는 상세 정보를 보여주는 코드 작성
        Toast.makeText(context, "상세 정보: ${date.date}", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        //val binding = FragmentCalendarBinding.inflate(inflater, container, false)

        //val calendarView = binding.calendarView

        // 예시: 2025년 1월 23일에 이미지를 설정
        //val targetDate = CalendarDay.from(2025, 1, 23)
        //val imageResId = R.drawable.ic_launcher_background // 배경으로 사용할 이미지

        // Decorator 적용
        //calendarView.addDecorator(CalendarBackgroundDecorator(imageResId, targetDate))

        //return binding.root

        // 배경 이미지를 추가하는 데코레이터 설정
        //calendarView.addDecorator(CalendarBackgroundDecorator(this))


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CalendarFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}