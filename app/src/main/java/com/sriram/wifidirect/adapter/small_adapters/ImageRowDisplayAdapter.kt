package com.sriram.wifidirect.adapter.small_adapters

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
import com.sriram.wifidirect.models.ImageModel
import kotlinx.android.synthetic.main.image_adapter.view.*
import java.io.File
import kotlin.random.Random


class ImageRowDisplayAdapter(private val context: Context, private val path: File, private val items:List<ImageModel>, private val listener:(ImageModel, Int)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private val TAG: String = "AppDebug"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.simple_image_adapter, parent, false)
        )
    }
    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder) {

            is BlogViewHolder -> {
                holder.bindItems(items.get(position),listener,path)
                val rnd = Random
                val color: Int =
                        Color.argb(255, rnd.nextInt(100), rnd.nextInt(100), rnd.nextInt(100))
                //   holder.cardView_loan_analysis.setCardBackgroundColor(color)
            }
        }


    }
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            Log.d("onclick","of item")
        }
    }

    class BlogViewHolder
    constructor(
            itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        val content_title = itemView.iv_image_content
        val content_cb=itemView.cb_mark_image
        val play_imgae=itemView.iv_play_image

        fun bindItems(blogPost: ImageModel, listener: (ImageModel, Int) -> Unit, path: File) {
            val position=adapterPosition

            val photoUri: Uri = Uri.fromFile(File(blogPost.path))
            val filepath= File(path.toString()+"/"+blogPost.path)
            if (blogPost.path.endsWith(".mp4")){
                play_imgae.visibility= View.VISIBLE
            }else{
                play_imgae.visibility= View.GONE
            }
            itemView.iv_image_content.load(filepath)

            val imageLoader = Coil.imageLoader(itemView.context)

            itemView.setOnClickListener {
                listener(blogPost,position)
            }



            }
        }

    inline fun Context.toast(message:String){
        Toast.makeText(this, message ,  Toast.LENGTH_LONG).show()
    }
}
