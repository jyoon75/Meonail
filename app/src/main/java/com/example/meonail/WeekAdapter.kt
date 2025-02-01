package com.example.meonail

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meonail.CalenderAdapter

class WeekAdapter(private val context: Context,
                  private val list: ArrayList<String>) : RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {

    private val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    inner class WeekViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtvDayOfWeek: TextView = itemView.findViewById(R.id.txtvDayOfWeek)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = inflater.inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.txtvDayOfWeek.text = list[position]
    }
}