package com.example.meonail.api

import com.example.meonail.model.WishListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("GGCULTUREVENTSTUS") // API 엔드포인트
    suspend fun getWishListData(
        @Query("KEY") apiKey: String,
        @Query("Type") responseType: String = "json",
        @Query("pIndex") pageIndex: Int = 1,
        @Query("pSize") pageSize: Int = 10
    ): Response<WishListResponse>
}
