package com.example.login
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.util.Log
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.room.Room
import com.example.login.databinding.ActivityCadastroBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CadastroActivity : ComponentActivity(), OnClickListener {

    val LOG: String = "LOG"

    lateinit var database: RoomDB_Login /*criando banco*/

    private lateinit var binding: ActivityCadastroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        Log.d("test", "onCreate")

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Room.databaseBuilder(applicationContext,
            RoomDB_Login::class.java,
            "loginDB").build() /*Inicializa o bd*/

        binding.btnCadastrar.setOnClickListener(this)
        binding.btnVoltar.setOnClickListener(this)
    }

    fun isValidCadastro(nome: String, email: String, senha: String, confirmarSenha: String): Boolean {
        if (nome.isEmpty()) {
            binding.txtErrorNome.error = "Digite o nome do usuário"
            return false
        } else if (email.isEmpty()) {
            binding.txtErrorEmail.error = "Digite o email"
            return false
        } else if (senha.isEmpty()) {
            binding.txtErrorSenha.error = "Digite a senha"
            return false
        } else if (confirmarSenha.isEmpty() || senha !== confirmarSenha) {
            binding.txtErrorConfirmarSenha.error = "As senhas devem ser igual"
            return false
        }

        return true
    }

    suspend fun InsertLogin(login: RoomEntity_Login) { /* Insere um novo registro*/
        database.loginDao().insert(login)
    }

    override fun onClick(v: View?) {
        try {
            when(v?.id) {
                R.id.btnCadastrar -> {

                    CoroutineScope(Dispatchers.IO).launch { /*Acessa banco*/

                        try {
                            val nome = binding.edtNome.text.toString().trim()
                            val email = binding.edtEmail.text.toString().trim()
                            val senha = binding.edtSenha.text.toString().trim()
                            val confirmarSenha = binding.edtConfirmarSenha.text.toString().trim()

                            if (isValidCadastro(nome, email, senha, confirmarSenha)) {

                                val entity: RoomEntity_Login = RoomEntity_Login(
                                    username = binding.edtNome.text.toString(),
                                    pwd = binding.edtSenha.text.toString(),
                                    email = binding.edtEmail.text.toString(),
                                    remember = false
                                )

                                database.loginDao().insert(entity) /*insere*/

                                Log.d("LOG", "saved")
                            }
                        } catch (exception: Exception) {
                            Log.d("LOG", "error: " + exception)
                        }
                    }
                }
                R.id.btnVoltar -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Por favor, insira valores válidos!", Toast.LENGTH_SHORT).show()
        }
    }
}

