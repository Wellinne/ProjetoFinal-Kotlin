package com.example.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.login.databinding.LoginsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Logins : AppCompatActivity() {

    private lateinit var binding: LoginsLayoutBinding
    private lateinit var database: RoomDB_Daily
    private lateinit var adapter: RecyclerViewDailyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginsLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuração do RecyclerView
        adapter = RecyclerViewDailyAdapter()
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter

        // Inicializando o banco de dados Room
        database = Room.databaseBuilder(applicationContext, RoomDB_Daily::class.java, "dailyDp").build()

        // Carregar os dados de forma assíncrona
        CoroutineScope(Dispatchers.IO).launch {
            val result: List<RoomEntity_Daily> = database.dailyDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.addAll(result)
            }
        }
    }

    // Função para inserir dados no banco
    suspend fun insertDaily(daily: RoomEntity_Daily) {
        database.dailyDao().insert(daily)
    }
}
