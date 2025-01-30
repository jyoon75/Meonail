package com.example.meonail.ui.home

import HomeViewModel
import RecordDatabaseHelper
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meonail.RecordInfoFragment
import com.example.meonail.RecordRegistActivity
import com.example.meonail.adapter.RecordAdapter
import com.example.meonail.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout
import java.security.AccessController.checkPermission

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // 어댑터를 클래스 레벨에서 선언
    private lateinit var adapter: RecordAdapter

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
        binding.recyclerViewRecords.layoutManager = LinearLayoutManager(requireContext())


        val recyclerView = binding.recyclerViewRecords
        recyclerView.layoutManager = LinearLayoutManager(requireContext())




        /*// 탭 설정
        // 초기 데이터 로드 (전체 기록)
        loadRecords("전체", "DESC")

        // Spinner(정렬) 설정
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortOrder = if (position == 0) "DESC" else "ASC"
                val selectedCategory = binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString()
                loadRecords(selectedCategory, sortOrder)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // TabLayout 설정
        setupTabs()

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = tab?.text.toString()
                val sortOrder = if (binding.spinnerSort.selectedItemPosition == 0) "DESC" else "ASC"
                loadRecords(category, sortOrder)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })*/
        // 탭 설정 및 데이터 로드
        setupTabs()

        /*binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = tab?.text.toString().substringBeforeLast(" (")
                loadRecords(category, getSelectedSortOrder())
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })*/
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = getSelectedCategory()
                val sortOrder = getSelectedSortOrder()
                loadRecords(category, sortOrder)  // 현재 정렬 방식 유지
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })


        /*binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadRecords(getSelectedCategory(), getSelectedSortOrder())
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }*/
        binding.spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortOrder = getSelectedSortOrder()
                val selectedCategory = getSelectedCategory()

                loadRecords(selectedCategory, sortOrder) // 현재 선택된 카테고리에 맞춰 정렬
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        homeViewModel.records.observe(viewLifecycleOwner) { records ->
            adapter = RecordAdapter(records) { recordId ->
                val intent = Intent(requireContext(), RecordInfoFragment::class.java).apply {
                    putExtra("record_id", recordId)
                }
                startActivity(intent)
            }
            binding.recyclerViewRecords.adapter = adapter
        }

        loadRecords("전체", "DESC")



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
            /*recyclerView.adapter = adapter*/
            binding.recyclerViewRecords.adapter = adapter
        }

        /*val intent = Intent(requireContext(), RecordRegistActivity::class.java)
        recordRegistLauncher.launch(intent)*/

        // Spinner의 아이템 선택에 따라 정렬 방식 처리
        val spinnerSort = binding.spinnerSort
        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sortOrder = when (position) {
                    0 -> "DESC"  // 최신순 (내림차순)
                    1 -> "ASC"   // 오래된순 (오름차순)
                    else -> "DESC"
                }
                // 데이터 로드
                /*homeViewModel.loadRecords(dbHelper, sortOrder)*/
                loadRecords(getSelectedCategory(), sortOrder)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무 것도 선택되지 않았을 때의 처리 (필요하면 구현)
            }
        }



        // 데이터 로드 (기본적으로 최신순으로 로드)
        homeViewModel.loadRecords(dbHelper, "DESC")

    }

    private fun setupTabs() {
        binding.tabLayout.removeAllTabs()
        val categories = dbHelper.getCategoriesWithCount()

        // "전체" 탭 추가
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("전체 (${categories.values.sum()})"))

        // 카테고리별 탭 추가
        categories.forEach { (category, count) ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setText("$category ($count)"))
        }
    }


    /*private fun loadRecords(category: String, sortOrder: String) {
        val records = if (category == "전체") {
            dbHelper.getAllRecords(sortOrder)
        } else {
            dbHelper.getRecordsByCategory(category, sortOrder)
        }
        adapter = RecordAdapter(records) { recordId ->
            val intent = Intent(requireContext(), RecordInfoFragment::class.java).apply {
                putExtra("record_id", recordId)
            }
            startActivity(intent)
        }
        binding.recyclerViewRecords.adapter = adapter
    }*/
    private fun loadRecords(category: String, sortOrder: String) {
        val records = if (category == "전체") {
            dbHelper.getAllRecords(sortOrder)
        } else {
            dbHelper.getRecordsByCategory(category, sortOrder)
        }
        homeViewModel.updateRecords(records)
    }


    private fun getSelectedCategory(): String {
        return binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString().substringBeforeLast(" (")
    }

    private fun getSelectedSortOrder(): String {
        return if (binding.spinnerSort.selectedItemPosition == 0) "DESC" else "ASC"
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
