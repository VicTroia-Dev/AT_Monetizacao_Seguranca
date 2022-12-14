package com.jafapps.firebasecursosf

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.br.jafapps.bdfirestore.util.Util
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jafapps.firebasecursosf.firestore_lista_categoria.Activity_Firestore_ListaCategoria

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        btn_gravar_dados.setOnClickListener(this)
        btn_visualizar_registros.setOnClickListener(this)
        btn_sair.setOnClickListener(this)

        permissao()
        ouvinteAutenticacao()


    }

    fun permissao(){

        val permissoes = arrayOf<String >(
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA
        )

        Util.permissao(this,100,permissoes)

    }

    fun ouvinteAutenticacao(){

        Firebase.auth.addAuthStateListener { authAtual ->

            if(authAtual.currentUser != null ){

                Util.exibirToast(baseContext,"Usuário Logado")
            }else{

                Util.exibirToast(baseContext,"Usuário Deslogado")
            }
        }

    }


    override fun onClick(p0: View?) {

        when(p0?.id){


            btn_gravar_dados.id -> {
                startActivity(Intent(this,Activity_Firestore_ListaCategoria::class.java))
            }

            btn_visualizar_registros.id-> {
                startActivity(Intent(this,Activity_Firestore_ListaCategoria::class.java))

            }

            btn_sair.id -> {

                finish()
                Firebase.auth.signOut()
                startActivity(Intent(this,AberturaActivity::class.java))
            }
            else -> false

        }

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        for( result in grantResults){
            if(result == PackageManager.PERMISSION_DENIED){

                Util.exibirToast(baseContext,"Aceite as permissões para funcionar o aplicativo")
                finish()
                break
            }
        }


    }

}