package com.example.meonail

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.meonail.databinding.ItemDayBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class CalenderAdapter(private val context: Context,
                      private val year: Int,
                      private val month: Int,
                      private val dates: ArrayList<CalenderDay>): RecyclerView.Adapter<CalenderAdapter.CalendarViewHolder>() {
    // 클릭 리스너 인터페이스
    private var mClickListener: ItemClickListener? = null

    // CalendarViewHolder 클래스 정의
    inner class CalendarViewHolder(val binding: ItemDayBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        var isInMonth: Boolean = false // 해당 날짜가 현재 월에 속하는지

        init {
            binding.root.setOnClickListener(this)
        }

        // 날짜가 클릭될 때 호출
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

            // 현재 날짜가 현재 월에 속하는지 여부 설정
            holder.isInMonth = currentDate.inMonth // CalenderDay의 inMonth 값으로 설정

            if (!currentDate.inMonth) {
                txtvDate.setTextColor(context.getColor(R.color.primary_ver1))
            } else {
                txtvDate.setTextColor(context.getColor(R.color.black)) // 기본 색상
            }

            // 이미지 설정 (image가 있을 경우만 설정)
            currentDate.image?.let {
                txtvDate.setTextColor(context.getColor(R.color.white))
                txtvDate.setShadowLayer(4F, 0F, 2F, R.color.primary_ver2)

                Log.d("RecordAdapter", "Loading Image URI: $it") // 디버깅 로그 추가

                Glide.with(context)
                    .load(it) // content:// URI 직접 로드
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // 캐싱 전략
                    .placeholder(R.drawable.default_calendar_background_image) // 로딩 중 기본 이미지
                    .error(R.drawable.default_calendar_background_image) // 실패 시 기본 이미지(이미지가 빈 기록 포함)
                    .into(imgDate)
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