package com.example.meonail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class MypageFragment : Fragment() {

    // 프래그먼트가 화면에 표시될 때 호출되는 메서드
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_mypage 레이아웃 파일을 화면에 표시
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        // 뷰 초기화
        val profileImage: ImageView = view.findViewById(R.id.profileImage) // 프로필 사진 뷰
        val nicknameText: TextView = view.findViewById(R.id.nicknameText) // 닉네임 텍스트 뷰
        val logoutButton: Button = view.findViewById(R.id.logoutButton)   // 로그아웃 버튼
        val wishlistButton: Button = view.findViewById(R.id.wishlistButton) // 위시리스트 버튼

        // 임시 데이터 설정 (실제 앱에서는 실제 데이터를 설정해야 함)
        profileImage.setImageResource(R.drawable.profile_img) // 프로필 사진을 설정
        nicknameText.text = "User"

        // 로그아웃 버튼 클릭 시 동작 설정
        logoutButton.setOnClickListener {
            // 로그아웃 로직 추가 (예: 사용자 세션 삭제 및 로그인 화면으로 이동)
        }

        // 위시리스트 버튼 클릭 시 동작 설정
        wishlistButton.setOnClickListener {
            // WishListActivity로 이동
            val intent = Intent(requireContext(), WishListActivity::class.java)
            startActivity(intent)
        }

        return view // 초기화된 뷰를 반환
    }
}
