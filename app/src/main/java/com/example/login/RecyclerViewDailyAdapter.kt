package com.example.login

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.LoginsLayoutBinding
import com.example.login.databinding.RowLayoutBinding

class RecyclerViewDailyAdapter : RecyclerView.Adapter<RecyclerViewDailyAdapter.DailyViewHolder>() {

    private var dailyList = ArrayList<RoomEntity_Daily>()

    fun updateList(daily: List<RoomEntity_Daily>) {
        Log.d("RecyclerView", "Atualizando lista com ${daily.size} itens")
        dailyList.clear()
        dailyList.addAll(daily)
        notifyDataSetChanged() // Atualiza toda a lista corretamente
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = RowLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily = dailyList[position]
        holder.bind(daily)
    }

    override fun getItemCount(): Int = dailyList.size

    inner class DailyViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: RoomEntity_Daily) {
            binding.textViewRow.setText(daily.title)
            binding.textViewEmail.setText(daily.description)
        }
    }
}