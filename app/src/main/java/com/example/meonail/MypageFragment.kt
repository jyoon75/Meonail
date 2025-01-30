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
import com.example.meonail.ui.login.LoginActivity
import com.example.meonail.util.SessionManager

class MypageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_mypage, container, false)

        val profileImage: ImageView = view.findViewById(R.id.profileImage)
        val nicknameText: TextView = view.findViewById(R.id.nicknameText)
        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        val wishlistButton: Button = view.findViewById(R.id.wishlistButton)

        // 사용자 세션 가져오기
        val sessionManager = SessionManager(requireContext())

        // 저장된 닉네임 불러오기 (없으면 기본값 "User")
        val nickname = sessionManager.getNickname() ?: "User"
        nicknameText.text = nickname

        // 로그아웃 버튼 클릭 시 동작
        logoutButton.setOnClickListener {
            sessionManager.clearSession() // 로그인 정보 삭제

            // 로그인 화면으로 이동
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)

            // 현재 액티비티(메인 화면) 종료
            requireActivity().finishAffinity()
        }

        // 위시리스트 버튼 클릭 시 이동
        wishlistButton.setOnClickListener {
            val intent = Intent(requireContext(), WishListActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}


