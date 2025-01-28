package com.example.meonail.adapter

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.meonail.MainActivity
import com.example.meonail.R
import com.example.meonail.RecordInfoFragment

/*class RecordAdapter(private val records: List<ContentValues>) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {*/
class RecordAdapter(
    private val records: List<ContentValues>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageViewThumbnail: ImageView = itemView.findViewById(R.id.imageViewThumbnail)
        val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val textViewTags: TextView = itemView.findViewById(R.id.textViewTags)
        val textViewNote: TextView = itemView.findViewById(R.id.textViewNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]

        // 제목 설정
        holder.textViewTitle.text = record.getAsString(RecordDatabaseHelper.COLUMN_TITLE)

        // 별점 설정
        holder.ratingBar.rating = record.getAsFloat(RecordDatabaseHelper.COLUMN_RATING)

        // 태그 설정
        val tags = record.getAsString(RecordDatabaseHelper.COLUMN_TAGS)
        // 태그가 비어 있으면 #을 표시하지 않도록
        if (tags.isNotEmpty()) {
            holder.textViewTags.text = tags.split(",")
                .filter { it.isNotBlank() }  // 공백 태그 제거
                .joinToString(" ") { "#$it" }
        } else {
            holder.textViewTags.text = "" // 태그가 없으면 공백 처리
        }

        // 내용 설정
        holder.textViewNote.text = record.getAsString(RecordDatabaseHelper.COLUMN_NOTE)

        // 이미지 썸네일 설정 (Glide 사용)
        val imageUris = record.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",") ?: listOf()
        Log.d("RecordAdapter", "Loaded Image URIs: $imageUris")
        if (imageUris.isNotEmpty()) {
            Glide.with(holder.imageViewThumbnail.context)
                .load(imageUris[0]) // 첫 번째 이미지 로드
                .placeholder(R.mipmap.ic_launcher) // 로드 중 기본 이미지
                .error(R.drawable.ic_dashboard_black_24dp) // 실패 시 기본 이미지
                .into(holder.imageViewThumbnail)
        } else {
            holder.imageViewThumbnail.setImageResource(R.drawable.ic_favorite_filled) // 기본 이미지
        }



        // 클릭 이벤트 처리
        holder.itemView.setOnClickListener {
            val recordId = record.getAsInteger(RecordDatabaseHelper.COLUMN_ID)
            onItemClick(recordId)
        }


    }

    override fun getItemCount(): Int = records.size
}