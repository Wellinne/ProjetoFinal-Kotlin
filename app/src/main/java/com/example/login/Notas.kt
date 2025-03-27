package com.example.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.login.databinding.LoginsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Notas : AppCompatActivity(), OnClickListener {

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

        binding.btnHome.setOnClickListener(this)
        binding.btnPerfil.setOnClickListener(this)
        binding.btnSair.setOnClickListener(this)

        // Inicializando o banco de dados Room
        database = Room.databaseBuilder(applicationContext, RoomDB_Daily::class.java, "dailyDp").build()

        // Carregar os dados de forma assíncrona
        CoroutineScope(Dispatchers.IO).launch {
            val result: List<RoomEntity_Daily> = database.dailyDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.updateList(result)
            }
        }

        // Set onClickListener para o botão Adicionar
        binding.btnAdd.setOnClickListener {
            val title = binding.textViewTitle.text.toString()
            val description = binding.textViewDescription.text.toString()

            if (title.isNotEmpty() && description.isNotEmpty()) {
                val currentDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())

                // Criar o objeto RoomEntity_Daily com a data atual
                val daily = RoomEntity_Daily(
                    title = title,
                    description = description,
                    date = currentDate
                )

                // Inserir no banco de dados
                CoroutineScope(Dispatchers.IO).launch {
                    insertDaily(daily)
                    // Atualizar RecyclerView após inserção
                    val result: List<RoomEntity_Daily> = database.dailyDao().getAll()
                    withContext(Dispatchers.Main) {
                        adapter.updateList(result)
                    }
                }
            } else {
                // Mostrar mensagem de erro ou algo similar se os campos estiverem vazios
            }
        }
    }

    // Função para inserir dados no banco
    suspend fun insertDaily(daily: RoomEntity_Daily) {
        database.dailyDao().insert(daily)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnHome -> {
                val email = intent.getStringExtra("EMAIL") ?: ""
                val intent = Intent(this, Notas::class.java)
                intent.putExtra("EMAIL", email);
                startActivity(intent)
                finish()
            }

            R.id.btnPerfil -> {
                val email = intent.getStringExtra("EMAIL") ?: ""

                val intent = Intent(this, PerfilActivity::class.java)
                intent.putExtra("EMAIL", email);
                startActivity(intent)
                finish()
            }

            R.id.btnSair -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}
