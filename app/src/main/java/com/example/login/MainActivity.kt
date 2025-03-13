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
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.room.RoomDatabase
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
                        // Busca o usuário no banco de dados pelo nome de usuário
                        val user = database.loginDao().getByEmail(email)

                        if (user != null && user.pwd == password) {
                            // Se o usuário existe e a senha está correta, faz login
                            withContext(Dispatchers.Main) {
                                // Redireciona para a tela de logins (ou outra tela)
                                val intent = Intent(this@MainActivity, Logins::class.java)
                                startActivity(intent)
                                finish() // Finaliza a MainActivity para evitar voltar para ela
                            }
                        } else {
                            // Se o usuário não existe ou a senha está incorreta, exibe uma mensagem de erro
                            withContext(Dispatchers.Main) {
                                binding.username.error = "Nome de usuário ou senha incorretos"
                                binding.pwd.error = "Nome de usuário ou senha incorretos"
                            }
                        }
                    } catch (exception: Exception) {
                        // Loga qualquer erro que ocorrer
                        Log.d("LOG", "Erro ao verificar login: ${exception.message}")
                    }
                }
            }
            R.id.register -> {
                val intent = Intent(this, CadastroActivity::class.java)
                startActivity(intent)
                finish()
                /*CoroutineScope(Dispatchers.IO).launch { /*Acessa banco*/

                    try {
                        val entity: RoomEntity_Login = RoomEntity_Login(
                            username = binding.username.text.toString(),
                            pwd = binding.pwd.text.toString(),
                            remember = binding.remember.isChecked
                        )

                        database.loginDao().insert(entity) /*insere*/

                        Log.d("LOG", "saved")

                    }catch (exception: Exception) {
                        Log.d("LOG", "error: " + exception)
                    }
                }*/
            }
        }
    }
}