package com.example.meonail.util

import android.content.Context
import android.content.SharedPreferences

/**
 * 사용자 세션을 관리하는 클래스
 * 로그인 상태 및 닉네임을 SharedPreferences에 저장하고 불러올 수 있음
 */
class SessionManager(context: Context) {
    // SharedPreferences 초기화 ("user_session"이라는 이름의 저장소 사용)
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    /**
     * 로그인 상태 저장
     * @param isLoggedIn 사용자가 로그인했는지 여부 (true: 로그인됨, false: 로그아웃됨)
     */
    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    /**
     * 로그인 상태 가져오기
     * @return 사용자의 로그인 상태 (true: 로그인됨, false: 로그아웃됨)
     */
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    /**
     * 세션 초기화 (로그아웃 시 사용)
     * SharedPreferences에 저장된 모든 데이터 삭제
     */
    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    /**
     * 닉네임 저장
     * @param nickname 사용자의 닉네임
     */
    fun saveNickname(nickname: String) {
        sharedPreferences.edit().putString("nickname", nickname).apply()
    }

    /**
     * 저장된 닉네임 가져오기
     * @return 저장된 닉네임 (없을 경우 null 반환)
     */
    fun getNickname(): String? {
        return sharedPreferences.getString("nickname", null)
    }
}

