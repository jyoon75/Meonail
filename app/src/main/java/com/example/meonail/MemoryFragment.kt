package com.example.meonail

import RecordDatabaseHelper
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meonail.databinding.FragmentMemoryBinding


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MemoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMemoryBinding
    private lateinit var databaseHelper: RecordDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMemoryBinding.inflate(inflater, container, false)
        databaseHelper = RecordDatabaseHelper(requireContext())

        // 클릭 시 MemoryGalleryActivity로 이동
        binding.layoutAllMemory.setOnClickListener { // 모든 기록
            val type = "All"
            val intent = Intent(requireContext(), MemoryGalleryActivity::class.java)
            intent.putExtra("type", type) // 넘기려는 값을 Intent에 담아서 전달
            startActivity(intent)
        }
        binding.layoutRatingFiveMemory.setOnClickListener { // 5.0점 기록
            val type = "RatingFive"
            val intent = Intent(requireContext(), MemoryGalleryActivity::class.java)
            intent.putExtra("type", type) // 넘기려는 값을 Intent에 담아서 전달
            startActivity(intent)
        }
        binding.layoutLongNoteMemory.setOnClickListener { // 노트가 긴 기록
            val type = "LongNote"
            val intent = Intent(requireContext(), MemoryGalleryActivity::class.java)
            intent.putExtra("type", type) // 넘기려는 값을 Intent에 담아서 전달
            startActivity(intent)
        }


        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MemoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}