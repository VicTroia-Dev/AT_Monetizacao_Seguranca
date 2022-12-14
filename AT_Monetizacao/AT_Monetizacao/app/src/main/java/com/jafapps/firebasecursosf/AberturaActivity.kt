package com.jafapps.firebasecursosf

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.br.jafapps.bdfirestore.util.Util
import com.google.firebase.auth.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_abertura.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AberturaActivity : AppCompatActivity(), View.OnClickListener {


    var auth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_abertura)

        button_Login.setOnClickListener(this)

        auth = Firebase.auth

        val user = auth?.currentUser


        if(user != null){
           val uid = user.uid
            finish()
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



    }

    override fun onClick(p0: View?) {

        when(p0?.id){

            button_Login.id -> {
                buttonLogin()
            }
            else -> false
        }

    }


    fun buttonLogin(){

        val email = editText_login.text.toString()
        val senha = editText_senha.text.toString()

        if( !email.trim().equals("") && !senha.trim().equals("")){

            if (Util.statusInternet(this)){

               GlobalScope.launch {
                   login(auth!!,email, senha)
             }

            }else{
                Util.exibirToast(this,"Você não possui uma conexão com a internet")
            }
        }else{

            Util.exibirToast(this,"Preencha todos os dados")
        }
    }

    suspend fun login(firebaseAuth: FirebaseAuth,
                               email:String,password:String): AuthResult? {
        return try{
            val data = firebaseAuth
                .signInWithEmailAndPassword(email,password)
                .await()
            val user = auth?.currentUser
            finish()
            val intent =  Intent(this,MainActivity::class.java)
            startActivity(intent)

            return data
        }catch (e : Exception){

            return null
        }

    }

    suspend fun Mensagem(context: Context, mensagem: String){

        Toast.makeText(context, mensagem , Toast.LENGTH_LONG).show()
    }


    fun errosFirebase(erro: String){


        if( erro.contains("There is no user record corresponding to this identifier")){

            Util.exibirToast(baseContext,"Esse e-mail não está cadastrado ainda")

        }
        else if( erro.contains("The password is invalid")){

            Util.exibirToast(baseContext,"Senha inválida")

        }
        else if(erro.contains("The email address is badly ")){

            Util.exibirToast(baseContext,"Este e-mail não é válido")

        }

    }

}