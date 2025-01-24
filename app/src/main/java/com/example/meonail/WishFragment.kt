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



import android.widget.Button


class WishFragment : Fragment() {
/*
    private lateinit var btnOpenWishList: Button // ✅ 버튼 추가
*/

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


/*
        btnOpenWishList = view.findViewById(R.id.btnOpenWishList) // ✅ 버튼 초기화 위치 확인!
*/


        fetchWishListData()

        // ✅ 상세 페이지 이동 이벤트 추가
        wishListAdapter.setOnItemClickListener { wishItem ->
            val intent = Intent(requireContext(), WishDetailActivity::class.java).apply {
                putExtra("wishItem", wishItem) // 🔥 선택한 아이템 전달
            }
            startActivity(intent) // 🔥 상세 페이지로 이동
        }

/*
        // ✅ 버튼 클릭 시 WishListActivity 실행
        btnOpenWishList.setOnClickListener {
            val intent = Intent(requireContext(), WishListActivity::class.java)
            startActivityForResult(intent, WISH_LIST_REQUEST) // 🔥 변경 사항 감지
        }
*/


        return view
    }


/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WISH_LIST_REQUEST && resultCode == RESULT_OK) {
            Log.d("WishFragment", "위시리스트 변경 감지, 데이터 새로고침")
            fetchWishListData() // 🔥 위시탭 데이터 새로고침
        }
    }
*/


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
    companion object {
        private const val WISH_LIST_REQUEST = 1001 // ✅ 위시리스트 요청 코드 추가
    }
}