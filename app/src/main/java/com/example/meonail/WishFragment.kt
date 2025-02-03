package com.example.meonail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.model.WishItem
import com.example.meonail.adapter.WishListAdapter
import com.example.meonail.api.RetrofitClient
import com.example.meonail.model.WishListResponse
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WishFragment : Fragment() {

    private lateinit var rvWishList: RecyclerView
    private lateinit var txtLoading: TextView
    private lateinit var wishListAdapter: WishListAdapter
    private val serviceKey = "5f2a4f19-de50-4c12-963c-bbb1e93138c4"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)
        rvWishList = view.findViewById(R.id.rvWishList)
        txtLoading = view.findViewById(R.id.txtLoading) // 🔥 로딩 메시지 추가

        wishListAdapter = WishListAdapter(requireContext(), isWishList = false)
        rvWishList.layoutManager = LinearLayoutManager(requireContext())
        rvWishList.adapter = wishListAdapter

        txtLoading.visibility = View.VISIBLE
        rvWishList.visibility = View.GONE

        fetchWishListData()

        wishListAdapter.setOnItemClickListener { wishItem ->
            val intent = Intent(requireContext(), WishDetailActivity::class.java).apply {
                putExtra("wishItem", wishItem)
            }
            startActivity(intent)
        }

        return view
    }

    private fun fetchWishListData() {
        val apiService = RetrofitClient.instance

        // 로딩 메시지 표시
        txtLoading.visibility = View.VISIBLE
        rvWishList.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val response: Response<WishListResponse> = apiService.getWishListData(
                    serviceKey,
                    pageNo = 1,
                    numOfRows = 10,
                    dtype = "전시",
                    title = "전시"
                )

                // ✅ API 요청 로그 추가 (API 주소 확인용)
                Log.d("API_REQUEST", "요청 URL: http://api.kcisa.kr/openapi/CNV_060/request?serviceKey=5f2a4f19-de50-4c12-963c-bbb1e93138c4&pageNo=1&numOfRows=10&dtype=전시&title=전시")

                // ✅ API 응답이 정상인지 확인
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    Log.d("API_RESPONSE", "Raw Response: $responseBody")

                    val wishItems = responseBody?.body?.items?.itemList ?: emptyList()
                    Log.d("PARSED_DATA", "파싱된 데이터: ${wishItems.size} 개의 아이템")

                    wishListAdapter.updateData(wishItems)

                    // 데이터 로딩 완료 -> RecyclerView 표시
                    txtLoading.visibility = View.GONE
                    rvWishList.visibility = View.VISIBLE
                } else {
                    Log.e("API_ERROR", "응답 실패: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", "네트워크 오류 발생", e)
            }
        }
    }
}
