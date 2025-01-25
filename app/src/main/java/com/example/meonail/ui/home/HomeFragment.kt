package com.example.meonail.ui.home

import HomeViewModel
import RecordDatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meonail.RecordInfoFragment
import com.example.meonail.adapter.RecordAdapter
import com.example.meonail.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    private lateinit var dbHelper: RecordDatabaseHelper

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

        /*homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records)
            recyclerView.adapter = adapter
        }*/
        /*homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records) { recordId ->
                // 클릭된 아이템의 ID로 상세 화면으로 이동
                val action = HomeFragmentDirections.actionHomeToRecordInfo(recordId)
                findNavController().navigate(action)
            }
            recyclerView.adapter = adapter
        }*/

        /*// RecyclerView 어댑터 설정
        homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records) { recordId ->
                // 클릭한 항목의 ID를 사용해 상세 페이지로 이동
                val action = HomeFragmentDirections.actionHomeToRecordInfo(recordId)
                findNavController().navigate(action)
            }
            recyclerView.adapter = adapter
        }*/


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

        /*// RecyclerView 어댑터 설정
        homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records) { recordId ->
                // NavController를 사용하여 Fragment 전환
                val action = HomeFragmentDirections.actionHomeToRecordInfo(recordId)
                findNavController().navigate(action)
            }
            recyclerView.adapter = adapter
        }*/

        /*homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records) { record_id ->
                // Safe Args를 통해 액션 호출
                val action = HomeFragmentDirections.actionHomeToRecordInfo(record_id)
                findNavController().navigate(action)  // 액션 실행
            }
            recyclerView.adapter = adapter
        }*/




        // 데이터 로드
        homeViewModel.loadRecords(dbHelper)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
