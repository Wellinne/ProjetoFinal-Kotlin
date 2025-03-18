package com.example.login

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewDailyAdapter : RecyclerView.Adapter<RecyclerViewDailyAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text_view_row)
        val textViewDate: TextView = view.findViewById(R.id.textViewDate)
    }

    val data: MutableList<RoomEntity_Daily> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = data[position].description
        holder.textViewDate.text = data[position].date
    }

    fun addAll(list: List<RoomEntity_Daily>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }
}