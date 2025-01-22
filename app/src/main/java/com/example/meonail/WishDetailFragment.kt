package com.example.meonail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.meonail.R
import com.example.meonail.model.WishItem

class WishDetailFragment : Fragment() {
    private lateinit var imgPoster: ImageView
    private lateinit var txtTitle: TextView
    private lateinit var txtCategory: TextView
    private lateinit var txtDate: TextView
    private lateinit var txtFee: TextView
    private lateinit var btnCreateImage: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_wish_detail, container, false)

        imgPoster = view.findViewById(R.id.imgWishPoster)
        txtTitle = view.findViewById(R.id.txtWishTitle)
        txtCategory = view.findViewById(R.id.txtWishCategory)
        txtDate = view.findViewById(R.id.txtWishDate)
        txtFee = view.findViewById(R.id.txtWishFee)
        btnCreateImage = view.findViewById(R.id.btnCreateImage)

        val wishItem = arguments?.getParcelable<WishItem>("wishItem")
        wishItem?.let { item ->
            txtTitle.text = item.title
            txtCategory.text = "카테고리: ${item.category}"
            txtDate.text = "일정: ${item.eventTime}"
            txtFee.text = "참가비: ${item.participationFee ?: "무료"}"

            Glide.with(this).load(item.imageUrl).into(imgPoster)

            // 🔹 버튼 클릭 시 이미지 생성 화면으로 이동
            btnCreateImage.setOnClickListener {
                val fragment = ImageCreationFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable("wishItem", item)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_image_creation_container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        return view
    }
}
