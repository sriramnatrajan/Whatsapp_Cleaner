package com.sriram.wifidirect.adapter

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.load
import com.sriram.wifidirect.R
import com.sriram.wifidirect.models.DocsModel
import com.sriram.wifidirect.models.FilesModel
import com.sriram.wifidirect.models.ImageModel
import kotlinx.android.synthetic.main.audio_file_adapter.view.*
import kotlinx.android.synthetic.main.image_adapter.view.*
import java.io.File
import kotlin.random.Random

class Files_adapter(private val context: Context,private val lists:List<DocsModel>,private val listener:(DocsModel,Int)->Unit)  : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val TAG: String = "AppDebug"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.audio_file_adapter, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return lists.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is BlogViewHolder -> {
                val movie: DocsModel = lists[position]
              //  holder.bindItems(movie,listener)
                 holder.bindItems(lists.get(position), listener)
            }
        }


    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            Log.d("onclick", "of item")
        }
    }

    class BlogViewHolder
    constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
       // val movie: mFilesModel = lists[position]

    fun bindItems(mFilesModel: DocsModel, listener: (DocsModel, Int) -> Unit) {
            val position = adapterPosition

        val image_name = itemView.iv_file_type_image
        val file_name=itemView.tv_file_type_name
        val file_size=itemView.tv_file_type_size

        file_name.text=mFilesModel.file_name
        file_size.text=mFilesModel.file_size.toString()
        image_name.load(mFilesModel.image)

        //image_name=mFilesModel.image
        //  val imageLoader = Coil.imageLoader(itemView.context)

            itemView.setOnClickListener {
                listener(mFilesModel,position)
            }


        }
    }

    inline fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
