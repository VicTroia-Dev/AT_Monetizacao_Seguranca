package com.jafapps.firebasecursosf.firestore_lista_categoria

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.br.jafapps.bdfirestore.util.DialogProgress
import com.br.jafapps.bdfirestore.util.Util
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jafapps.firebasecursosf.R
import com.jafapps.firebasecursosf.firestore_lista_item.Activity_Firestore_ListaItem
import kotlinx.android.synthetic.main.activity_firestore_listacategoria.*

class Activity_Firestore_ListaCategoria : AppCompatActivity(), SearchView.OnQueryTextListener,
    SearchView.OnCloseListener, AdapterRecyclerView_Categoria.ClickCategoria,
    AdapterRecyclerView_Categoria.UltimoItemExibidoRecyclerView {

    var searchView: SearchView? = null

    var adapterRecyclerViewCategoria: AdapterRecyclerView_Categoria? = null
    var categorias: ArrayList<Categoria> = ArrayList()

    var database: FirebaseFirestore? = null
    var reference: CollectionReference? = null
    var proximoQuery: Query? = null

    var isFiltrando = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firestore_listacategoria)


        supportActionBar?.title = "Categorias"

        btn_exibirMais.visibility = View.GONE

        database = FirebaseFirestore.getInstance()
        reference = database?.collection("Categorias")

        iniciarRecyclerView()


        exibirPrimeirosItensBD()

    }

    fun iniciarRecyclerView(){


        adapterRecyclerViewCategoria =AdapterRecyclerView_Categoria(baseContext,categorias,this,this)


        recyclerView_Firestore_ListaCategoria.layoutManager = LinearLayoutManager(this)
        recyclerView_Firestore_ListaCategoria.adapter = adapterRecyclerViewCategoria

    }

    override fun clickCategoria(categoria: Categoria) {



        val intent = Intent(this,Activity_Firestore_ListaItem::class.java)
        intent.putExtra("categoriaNome",categoria)

        startActivity(intent)


    }

    override fun ultimoItemExibidoRecyclerView(isExibido: Boolean) {


        if(isFiltrando){

            //     Util.exibirToast(this,"Você está filtrando. Não vai ser exibido mais itens")

        }else{

            exibirMaisItensBD()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.search,menu)

        val search = menu!!.findItem(R.id.action_search)

        searchView = search.actionView as SearchView

        searchView?.queryHint = "Pesquisar nome"

        searchView?.setOnQueryTextListener(this)
        searchView?.setOnCloseListener(this)
        searchView?.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {


        Log.d("yyyyui-onQueryTextS","onQueryTextSubmit")

        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {

        Log.d("yyyyui-onQueryTextC",newText.toString())


        isFiltrando = true
        pesquisarNome(newText.toString())

        return true
    }

    override fun onClose(): Boolean {

        isFiltrando = false

        searchView?.onActionViewCollapsed()

        categorias.clear()
        adapterRecyclerViewCategoria?.notifyDataSetChanged()

        exibirPrimeirosItensBD()

        return true
    }

    fun pesquisarNome(newText: String){

        val query = database!!.collection("Categorias")
            .orderBy("nome").startAt(newText).endAt(newText+"\uf8ff").limit(3)

        query.get().addOnSuccessListener { documentos ->

            categorias.clear()

            for(documento in documentos){


                val categoria = documento.toObject(Categoria::class.java)
                categorias.add(categoria)
            }

            adapterRecyclerViewCategoria?.notifyDataSetChanged()

        }

    }

    fun exibirPrimeirosItensBD(){


        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager,"1")


        var query = database!!.collection("Categorias").orderBy("nome").limit(10)



        query.get().addOnSuccessListener { documentos ->


            dialogProgress.dismiss()


            val ultimoDOcumento = documentos.documents[documentos.size() - 1]
            proximoQuery = database!!.collection("Categorias").orderBy("nome").startAfter(ultimoDOcumento).limit(10)


            for(documento in documentos){

                var categoria = documento.toObject(Categoria::class.java)
                categorias.add(categoria)
            }


            adapterRecyclerViewCategoria?.notifyDataSetChanged()


        }.addOnFailureListener {error ->


            Util.exibirToast(baseContext,"Error : ${error.message}")
            dialogProgress.dismiss()
        }

    }

    fun exibirMaisItensBD(){

        val dialogProgress = DialogProgress()
        dialogProgress.show(supportFragmentManager,"1")

        proximoQuery!!.get().addOnSuccessListener { documentos ->


            dialogProgress.dismiss()

            if(documentos.size() > 0 ){

                val ultimoDOcumento = documentos.documents[documentos.size() - 1]
                proximoQuery = database!!.collection("Categorias").orderBy("nome").startAfter(ultimoDOcumento).limit(10)


                for(documento in documentos){

                    val categoria = documento.toObject(Categoria::class.java)
                    categorias.add(categoria)
                }

                adapterRecyclerViewCategoria?.notifyDataSetChanged()

            }else{

            }

        }.addOnFailureListener {error ->

            Util.exibirToast(baseContext,"Error : ${error.message}")
            dialogProgress.dismiss()

        }
    }
}