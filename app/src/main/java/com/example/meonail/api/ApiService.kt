package com.example.meonail.api

import com.example.meonail.model.WishListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("CNV_060/request") // ✅ XML API 요청
    suspend fun getWishListData(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("dtype") dtype: String = "전시",
        @Query("title") title: String = ""
    ): Response<WishListResponse>
}
