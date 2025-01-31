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


    // 데이터 로딩
    override fun onResume() {
        super.onResume()
        loadRecords("전체", "DESC") // 홈 화면에서 데이터를 다시 불러오기
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





        // 탭 설정 및 데이터 로드
        setupTabs()


        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val category = getSelectedCategory()
                val sortOrder = getSelectedSortOrder()
                loadRecords(category, sortOrder)  // 현재 정렬 방식 유지
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })



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
