package com.example.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.room.Room
import com.example.login.databinding.ActivityPerfilBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilActivity : ComponentActivity(), View.OnClickListener {

    private lateinit var binding: ActivityPerfilBinding
    private lateinit var database: RoomDB_Login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializa o banco de dados
        database = Room.databaseBuilder(
            applicationContext,
            RoomDB_Login::class.java,
            "loginDB"
        ).build()

        // Pegando o username do usuário logado (passado pela Intent)
        val username = intent.getStringExtra("EMAIL") ?: ""
        Log.d("LOG", username)

        if (username.isNotEmpty()) {
            buscarUsuario(username)
        } else {
            Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_SHORT).show()
        }

        binding.btnVoltarPerfil.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnVoltarPerfil -> {
                val email = intent.getStringExtra("EMAIL") ?: ""
                val intent = Intent(this, Notas::class.java)

                intent.putExtra("EMAIL", email);
                startActivity(intent)
                finish()
            }
        }
    }

    // Função para buscar usuário no banco de dados e exibir nos TextViews
    private fun buscarUsuario(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val usuario = database.loginDao().getByEmail(username)

                if (usuario.email.isNotEmpty() && usuario.username.isNotEmpty() && usuario.pwd.isNotEmpty()) {
                    withContext(Dispatchers.Main) {
                        binding.txtNomePerfil.text = usuario.username
                        binding.txtEmailPerfil.text = usuario.email
                        binding.txtSenhaPerfil.text = usuario.pwd
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@PerfilActivity, "Usuário não encontrado", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("PerfilActivity", "Erro ao buscar usuário", e)
            }
        }
    }
}
