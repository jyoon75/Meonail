package com.example.meonail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.meonail.R
import com.example.meonail.model.WishItem

class ImageCreationFragment : Fragment() {
    private lateinit var imgGeneratedImage: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_image_creation, container, false)
        imgGeneratedImage = view.findViewById(R.id.imgGeneratedImage)

        // ✅ WishItem 데이터 가져오기
        val wishItem: WishItem? = arguments?.getParcelable("wishItem")

        wishItem?.let {
            val bitmap = createCustomImage(it.title, it.imageUrl ?: "") // ✅ null 체크
            imgGeneratedImage.setImageBitmap(bitmap)

            // ✅ Glide를 사용해 이미지 로딩 (imageUrl이 null이면 기본 이미지 표시)
            Glide.with(this)
                .load(it.imageUrl ?: R.drawable.ic_launcher_background)
                .into(imgGeneratedImage)
        }

        return view
    }

    private fun createCustomImage(text: String, imageUrl: String?): Bitmap {
        val bitmap = Bitmap.createBitmap(800, 1000, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        paint.color = Color.BLACK
        paint.textSize = 50f
        canvas.drawText(text, 50f, 600f, paint)
        return bitmap
    }
}
