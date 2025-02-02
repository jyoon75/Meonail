package com.example.meonail

import android.content.ContentValues
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MemoryAdapter(fragmentActivity: FragmentActivity, private val records: List<ContentValues>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = records.size

    override fun createFragment(position: Int): Fragment {
        val record = records[position]
        val fragment = RecordInfoFragment()
        val bundle = Bundle().apply {
            putInt("record_id", record.getAsInteger(RecordDatabaseHelper.COLUMN_ID))
        }
        fragment.arguments = bundle
        return fragment
    }
}