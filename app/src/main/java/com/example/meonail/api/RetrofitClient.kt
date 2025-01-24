package com.example.meonail.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "http://api.kcisa.kr/openapi/"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS) // 🔥 연결 타임아웃 60초로 증가
        .readTimeout(60, TimeUnit.SECONDS)    // 🔥 읽기 타임아웃 60초로 증가
        .writeTimeout(60, TimeUnit.SECONDS)   // 🔥 쓰기 타임아웃 60초로 증가
        .retryOnConnectionFailure(true)       // 🔥 연결 실패 시 자동 재시도
        .build()

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // 🔥 OkHttpClient 적용
            .addConverterFactory(SimpleXmlConverterFactory.create()) // XML 파싱 설정 유지
            .build()
            .create(ApiService::class.java)
    }
}
