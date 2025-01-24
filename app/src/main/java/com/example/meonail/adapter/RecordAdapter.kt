package com.example.meonail.adapter

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.R
import com.example.meonail.RecordInfoFragment

class RecordAdapter(private val records: List<ContentValues>) :
    RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

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
        holder.textViewTags.text = tags.split(",").joinToString(" ") { "#$it" }

        // 내용 설정
        holder.textViewNote.text = record.getAsString(RecordDatabaseHelper.COLUMN_NOTE)

        // 이미지 썸네일 설정 (첫 번째 이미지 사용)
        val imageUris =
            record.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",") ?: listOf()
        if (imageUris.isNotEmpty()) {
            holder.imageViewThumbnail.setImageURI(Uri.parse(imageUris[0]))
        } else {
            holder.imageViewThumbnail.setImageResource(R.mipmap.ic_launcher) // 기본 이미지
        }

        // 아이템 클릭 시 상세 보기로 이동
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, RecordInfoFragment::class.java).apply {
                putExtra("record_id", record.getAsInteger(RecordDatabaseHelper.COLUMN_ID))
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = records.size
}