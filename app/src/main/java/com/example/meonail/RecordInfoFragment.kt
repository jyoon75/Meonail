package com.example.meonail

import RecordDatabaseHelper
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class RecordInfoFragment : Fragment(R.layout.fragment_record_info) {

    //private val args: RecordInfoFragmentArgs by navArgs() // Safe Args를 통해 인자 받기


    private var recordId: Int? = null
    private lateinit var databaseHelper: RecordDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recordId = arguments?.getInt("record_id")
        databaseHelper = RecordDatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_record_info, container, false)

        // UI 요소 초기화
        val imageViewThumbnailInfo = view.findViewById<ImageView>(R.id.imageViewThumbnailInfo)
        val textViewTitleInfo = view.findViewById<TextView>(R.id.textViewTitleInfo)
        val ratingBarInfo = view.findViewById<RatingBar>(R.id.ratingBarInfo)
        val textViewTagsInfo = view.findViewById<TextView>(R.id.textViewTagsInfo)
        val textViewNoteInfo = view.findViewById<TextView>(R.id.textViewNoteInfo)

        // 데이터베이스에서 기록 정보 불러오기
        recordId?.let {
            val record = databaseHelper.getRecordById(it)
            record?.let { data ->
                textViewTitleInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_TITLE)
                ratingBarInfo.rating = data.getAsFloat(RecordDatabaseHelper.COLUMN_RATING)
                val tags = data.getAsString(RecordDatabaseHelper.COLUMN_TAGS)
                textViewTagsInfo.text = tags.split(",").joinToString(" ") { "#$it" }
                textViewNoteInfo.text = data.getAsString(RecordDatabaseHelper.COLUMN_NOTE)

                // 이미지 설정
                val imageUris = data.getAsString(RecordDatabaseHelper.COLUMN_IMAGES)?.split(",")
                if (!imageUris.isNullOrEmpty()) {
                    imageViewThumbnailInfo.setImageURI(Uri.parse(imageUris[0]))
                }
            }
        }

        return view
    }



    /*override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recordId = args.recordId // Safe Args를 통해 넘겨받은 값
        // 이제 recordId를 사용하여 데이터를 로드하거나 표시
    }*/



    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}
