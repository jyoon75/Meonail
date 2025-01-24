package com.example.meonail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.R
import com.example.meonail.api.RetrofitClient
import com.example.meonail.model.WishItem
import com.example.meonail.model.WishListResponse
import com.example.meonail.adapter.WishListAdapter
import kotlinx.coroutines.launch
import retrofit2.Response

class WishFragment : Fragment() {

    private lateinit var rvWishList: RecyclerView
    private val wishListAdapter by lazy { WishListAdapter(requireContext()) } // ✅ 바로 초기화

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)
        rvWishList = view.findViewById(R.id.rvWishList)
        rvWishList.layoutManager = LinearLayoutManager(requireContext())
        rvWishList.adapter = wishListAdapter

        fetchWishListData()

        // ✅ 클릭 이벤트 추가
        wishListAdapter.setOnItemClickListener { wishItem ->
            val intent = Intent(requireContext(), WishDetailActivity::class.java).apply {
                putExtra("wishItem", wishItem)
            }
            startActivity(intent) // ✅ 새로운 화면(액티비티)으로 이동
        }

        return view
    }

    private fun fetchWishListData() {
        val apiService = RetrofitClient.instance

        lifecycleScope.launch {
            try {
                val response: Response<WishListResponse> = apiService.getWishListData(
                    "607f1cc83e054d7aadd6ad4af6d00e36",
                    "json",
                    1,
                    10
                )

                Log.d("API_CALL", "응답 코드: ${response.code()}")
                Log.d("API_CALL", "응답 바디: ${response.body()}")

                if (response.isSuccessful) {
                    response.body()?.eventList?.flatMap { it.events ?: emptyList() }?.let { data ->
                        Log.d("API_RESPONSE", "Parsed Data: $data")

                        Log.d("WishFragment", "RecyclerView 아이템 개수: ${wishListAdapter.itemCount}")


                        wishListAdapter.updateData(data)
                        rvWishList.post { wishListAdapter.notifyDataSetChanged() } // 🔥 RecyclerView 갱신
                    }
                } else {
                    Log.e("API_ERROR", "오류 코드: ${response.code()}, 메시지: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("WishFragment", "네트워크 오류 발생", e)
            }
        }
    }
}
