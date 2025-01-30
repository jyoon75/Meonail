package com.example.meonail.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("is_logged_in", isLoggedIn).apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("is_logged_in", false)
    }

    fun clearSession() {
        sharedPreferences.edit().clear().apply()
    }

    // 닉네임 저장
    fun saveNickname(nickname: String) {
        sharedPreferences.edit().putString("nickname", nickname).apply()
    }

    // 닉네임 가져오기
    fun getNickname(): String? {
        return sharedPreferences.getString("nickname", null)
    }
}

