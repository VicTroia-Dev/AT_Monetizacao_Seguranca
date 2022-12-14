package com.jafapps.firebasecursosf.firestore_lista_categoria

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jafapps.firebasecursosf.R
import kotlinx.android.synthetic.main.item_categoria_recyclerview.view.*


class AdapterRecyclerView_Categoria  (val context: Context, var categorias: ArrayList<Categoria>,
                                      var clickCategoria: ClickCategoria, var ultimoItemExibidoRecyclerView: UltimoItemExibidoRecyclerView):
    RecyclerView.Adapter<AdapterRecyclerView_Categoria.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_categoria_recyclerview,parent,false)

        val holder = ViewHolder(view)

        return holder

    }

    override fun getItemCount(): Int {

       return categorias.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val categoria: Categoria = categorias.get(position)

        holder.nome.text = categoria.nome

        holder.cardView.setOnClickListener{

            clickCategoria.clickCategoria(categoria)

        }


        if(position == getItemCount() - 1){


            ultimoItemExibidoRecyclerView.ultimoItemExibidoRecyclerView(true)

        }


    }

    interface ClickCategoria{


        fun clickCategoria(categoria: Categoria)

    }

    interface  UltimoItemExibidoRecyclerView {


        fun ultimoItemExibidoRecyclerView(isExibido: Boolean)


    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val nome = itemView.textView_ListaItemCategoria_Nome
        val cardView = itemView.cardView_padrao_categoria

    }


}