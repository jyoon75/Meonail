package com.example.meonail.ui.home

import HomeViewModel
import RecordDatabaseHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meonail.RecordInfoFragment
import com.example.meonail.RecordRegistActivity
import com.example.meonail.adapter.RecordAdapter
import com.example.meonail.databinding.FragmentHomeBinding
import java.security.AccessController.checkPermission

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var dbHelper: RecordDatabaseHelper

    // 저장 액티비티 실행 후 결과 처리
    private val recordRegistLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 데이터 갱신 로직 호출
            /*homeViewModel.loadRecords(dbHelper) // DB에서 다시 데이터를 가져옴*/
            val newRecordId = result.data?.getLongExtra("new_record_id", -1L)?.toInt()
            if (newRecordId != null && newRecordId > 0) {
                // 새로 추가된 레코드만 가져와서 갱신
                val newRecord = dbHelper.getRecordById(newRecordId)
                val updatedRecords = homeViewModel.records.value?.toMutableList() ?: mutableListOf()
                newRecord?.let { updatedRecords.add(0, it) } // 새 레코드를 맨 앞에 추가
                homeViewModel.updateRecords(updatedRecords) // 데이터 갱신
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = RecordDatabaseHelper(requireContext())

        val recyclerView = binding.recyclerViewRecords
        recyclerView.layoutManager = LinearLayoutManager(requireContext())



        // 기록 상세보기
        // RecyclerView 어댑터 설정
        homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records) { record_id ->
                // 클릭된 아이템의 ID로 상세 화면 실행
                val intent = Intent(requireContext(), RecordInfoFragment::class.java).apply {
                    putExtra("record_id", record_id)
                }
                startActivity(intent)
            }
            recyclerView.adapter = adapter
        }

        /*val intent = Intent(requireContext(), RecordRegistActivity::class.java)
        recordRegistLauncher.launch(intent)*/



        // 데이터 로드
        homeViewModel.loadRecords(dbHelper)

    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
