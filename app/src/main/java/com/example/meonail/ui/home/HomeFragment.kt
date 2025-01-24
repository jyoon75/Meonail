package com.example.meonail.ui.home

import HomeViewModel
import RecordDatabaseHelper
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
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

        homeViewModel.records.observe(viewLifecycleOwner) { records ->
            val adapter = RecordAdapter(records)
            recyclerView.adapter = adapter
        }

        homeViewModel.loadRecords(dbHelper)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
