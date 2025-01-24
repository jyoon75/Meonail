package com.example.meonail

import android.app.Activity.RESULT_OK
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
    private lateinit var wishListAdapter: WishListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_wish, container, false)
        rvWishList = view.findViewById(R.id.rvWishList)
        wishListAdapter = WishListAdapter(requireContext(), isWishList = false) // 🔥 위시탭에서는 삭제 X
        rvWishList.layoutManager = LinearLayoutManager(requireContext())
        rvWishList.adapter = wishListAdapter

        fetchWishListData()

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

                if (response.isSuccessful) {
                    response.body()?.eventList?.flatMap { it.events ?: emptyList() }?.let { data ->
                        wishListAdapter.updateData(data)
                    }
                }
            } catch (e: Exception) {
                Log.e("WishFragment", "네트워크 오류 발생", e)
            }
        }
    }
}
