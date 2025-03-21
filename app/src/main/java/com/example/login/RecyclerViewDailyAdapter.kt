package com.example.login

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.login.databinding.LoginsLayoutBinding

class RecyclerViewDailyAdapter : RecyclerView.Adapter<RecyclerViewDailyAdapter.DailyViewHolder>() {

    private var dailyList = mutableListOf<RoomEntity_Daily>()

    fun addAll(daily: List<RoomEntity_Daily>) {
        Log.d("RecyclerView", "Adicionando ${daily.size} itens")
        dailyList.addAll(daily)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyViewHolder {
        val binding = LoginsLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val daily = dailyList[position]
        holder.bind(daily)
    }

    override fun getItemCount(): Int = dailyList.size

    inner class DailyViewHolder(private val binding: LoginsLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(daily: RoomEntity_Daily) {
            // Usando TextView para exibir texto
            binding.textViewTitle.setText(daily.title)
            binding.textViewDescription.setText(daily.description)
        }
    }
}
