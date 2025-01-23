package com.example.meonail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.databinding.ItemDayBinding

class CalenderAdapter(private val context: Context,
                      private val year: Int,
                      private val month: Int,
                      private val dates: ArrayList<CalenderDay>): RecyclerView.Adapter<CalenderAdapter.CalendarViewHolder>() {
    // 클릭 리스너 인터페이스
    private var mClickListener: ItemClickListener? = null

    // CalendarViewHolder 클래스 정의
    inner class CalendarViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var isInMonth: Boolean = true //해당 날짜가 현재 월에 속하는지

        init {
            binding.root.setOnClickListener(this)
        }

        //날짜가 클릭될 때 호출
        override fun onClick(view: View?) {
            mClickListener?.onItemClick(view, binding.txtvDate.text.toString(), isInMonth)
        }

    }

    // 아이템 뷰 생성 시 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CalendarViewHolder(binding)
    }

    // 데이터와 아이템 뷰 연결
    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val currentDate = dates[position]

        holder.binding.apply {
            // 날짜 텍스트 설정
            txtvDate.text = currentDate.day.toString()

            // 이미지 설정 (image가 있을 경우만 설정)
            currentDate.image?.let {
                imgDate.setImageBitmap(it)  // 이미지가 있을 경우
            } ?: run {
                //imgDate.setImageResource(R.drawable.default_image)  // 기본 이미지 설정
            }
        }
    }

    // 아이템 개수 반환
    override fun getItemCount(): Int = dates.size

    // 클릭 리스너
    interface ItemClickListener {
        fun onItemClick(view: View?, day: String, isInMonth: Boolean)
    }
    fun setClickListener(itemClickListener: ItemClickListener) {
        this.mClickListener = itemClickListener
    }
}