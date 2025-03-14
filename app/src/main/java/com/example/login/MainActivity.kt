package com.example.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.room.Room
import com.example.login.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnClickListener {

    lateinit var database: RoomDB_Login

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        database = Room.databaseBuilder(applicationContext,
            RoomDB_Login::class.java,
        "loginDB").build() /*Inicializa o bd*/

        binding.login.setOnClickListener(this)
        binding.register.setOnClickListener(this)
    }

    suspend fun InsertLogin(login: RoomEntity_Login) { /* Insere um novo registro*/
        database.loginDao().insert(login)
    }

    suspend fun GetByUsername(username: String): RoomEntity_Login {/*Get regista pelo nome de usuário*/
        return database.loginDao().getByUsername(username)
    }

    suspend fun GetByEmail(email: String): RoomEntity_Login {/*Get pelo email de usuário*/
        return database.loginDao().getByEmail(email)
    }


    suspend fun UpdateLogin(username: String, password: String) {/*Atualiza registro*/
        database.loginDao().update(username, password)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.login -> {
                val email = binding.username.text.toString()
                val password = binding.pwd.text.toString()

                if (email.isEmpty() || password.isEmpty()) {
                    // Verifica se os campos estão vazios
                    binding.username.error = "Preencha o nome de usuário"
                    binding.pwd.error = "Preencha a senha"
                    return
                }

                // Inicia uma coroutine para verificar o login no banco de dados
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val user = database.loginDao().getByEmail(email)
                        if (user == null || user.pwd != password) {
                            withContext(Dispatchers.Main) {
                                binding.username.error = "Nome de usuário ou senha incorretos"
                                binding.pwd.error = "Nome de usuário ou senha incorretos"
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                val intent = Intent(this@MainActivity, Logins::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } catch (exception: Exception) {
                        Log.d("LOG", "Erro ao verificar login: ${exception.message}")
                    }
                }
            }
            R.id.register -> {
                val intent = Intent(this, CadastroActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}