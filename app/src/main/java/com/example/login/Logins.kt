package com.example.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.login.databinding.ActivityMainBinding
import com.example.login.databinding.LoginsLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Logins: AppCompatActivity() {
    lateinit var binding: LoginsLayoutBinding

    lateinit var database: RoomDB_Login /*acessar banco*/

    lateinit var adapter: RecyclerViewLoginAdapter /*Declara uma variável adapter para gerenciar a exibição dos dados em um RecyclerView*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginsLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecyclerViewLoginAdapter() /* Inicializa o adaptador (RecyclerViewLoginAdapter) que será usado para preencher o RecyclerView*/
        binding.list.layoutManager = LinearLayoutManager(this)
        binding.list.adapter = adapter

        database = Room.databaseBuilder(applicationContext,
            RoomDB_Login::class.java,
            "loginDB").build() /* Inicializa e cria o banco*/

        CoroutineScope(Dispatchers.IO).launch {
            val result: List<RoomEntity_Login> = database.loginDao().getAll() /*pega todos os registros*/

            withContext(Dispatchers.Main) {
                adapter.addAll(result) /*Passa a lista de registros de login (result) para o adaptador (RecyclerViewLoginAdapter), que atualiza o RecyclerView com os dados.*/
            }
        }
    }

}