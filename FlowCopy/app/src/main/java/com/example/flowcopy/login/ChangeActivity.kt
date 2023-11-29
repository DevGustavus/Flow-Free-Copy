package com.example.flowcopy.login

import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.example.flowcopy.DAO.Conta
import com.example.flowcopy.DAO.DAO_Conta
import com.example.flowcopy.DAO.MyDataBaseHelper
import com.example.flowcopy.R
import com.example.flowcopy.databinding.ActivityChangeBinding

class ChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        // Verificar se a versão do Android é igual ou superior a Lollipop (API 21)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Definir a cor desejada para a barra de notificação
            window.statusBarColor = getColor(R.color.black) // Substitua "sua_cor" pelo ID real da sua cor
        }

        //Carregamento dos dados do banco
        val banco_contas = MyDataBaseHelper(applicationContext)
        val operacoesBanco = DAO_Conta(banco_contas)

        val conta: Conta? = operacoesBanco.retornarContaLogada()
        if (conta != null){
            binding.editTextUsername.setText(conta.nome)
            binding.editTextEmail.setText(conta.email)
            binding.editTextPassword.setText(conta.senha)
        }

        binding.buttonChange.setOnClickListener(){

            fecharTeclado()

            val username = binding.editTextUsername.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            if (conta != null){
                if (username != "" && email != "" && password != ""){
                    conta.nome = username
                    conta.email = email
                    conta.senha = password

                    if (operacoesBanco.atualizarConta(conta)) {
                        limparCampos(binding.editTextUsername,binding.editTextEmail, binding.editTextPassword)
                        val msg = "Conta alterada com sucesso!"
                        binding.popUp.text = msg
                        binding.popUp.visibility = View.VISIBLE
                        binding.popUp.postDelayed({binding.popUp.visibility = View.INVISIBLE},2000)
                        binding.popUp.postDelayed({ finish() }, 2000)
                    }else {
                        val msg = "Erro no Update.\nTente novamente."
                        binding.popUp.text = msg
                        binding.popUp.visibility = View.VISIBLE
                        binding.popUp.postDelayed({binding.popUp.visibility = View.INVISIBLE},2000)
                    }
                }else {
                    val msg = "Algo está incorreto.\nVerifique e tente novamente."
                    binding.popUp.text = msg
                    binding.popUp.visibility = View.VISIBLE
                    binding.popUp.postDelayed({binding.popUp.visibility = View.INVISIBLE},2000)
                }

            }

        }

        binding.backView.setOnClickListener(){
            finish()
        }
    }

    private fun limparCampos(campo1 : EditText, campo2 : EditText, campo3 : EditText){
        campo1.text.clear()
        campo2.text.clear()
        campo3.text.clear()
    }

    private fun fecharTeclado() {
        val view: View? = currentFocus
        if (view != null) {
            val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}