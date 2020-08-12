package com.sriram.wifidirect.adapter



import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.api.load
import com.bumptech.glide.Glide
import com.sriram.wifidirect.R
import com.sriram.wifidirect.models.ImageModel
import kotlinx.android.synthetic.main.image_adapter.view.*

import java.io.File
import kotlin.random.Random


/**
 * Created by Sriram.N
 */


class ContentRecyclerAdapter(private val context: Context,private val path:File,private val items:List<ImageModel>,private val listener:(ImageModel,Int)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{

    private val TAG: String = "AppDebug"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BlogViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_adapter, parent, false)
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
       val contentcontent_cb_title = itemView.iv_image_content
       val content_cb=itemView.cb_mark_image
        val play_imgae=itemView.iv_play_image
        val vid_length=itemView.vid_length
        val play_image=itemView.iv_play_image
        val img_layout=itemView.image_layout
        val content_image= itemView.iv_image_content
        fun bindItems(blogPost: ImageModel, listener: (ImageModel, Int) -> Unit, path: File) {
            try {
            val position=adapterPosition

            val photoUri: Uri = Uri.fromFile(File(blogPost.path))
            val filepath=File(path.toString()+"/"+blogPost.path)
                Log.d("file names",filepath .toString())
            if (blogPost.path.endsWith(".mp4")){
                play_imgae.visibility=View.VISIBLE
                img_layout.visibility=View.VISIBLE
                play_image.visibility=View.INVISIBLE
                 Glide.with(itemView.context).load(filepath).into(content_image);
                val mp: MediaPlayer = MediaPlayer.create(itemView.context, Uri.parse(filepath.toString()))
                val duration = mp.duration
                mp.release()
                vid_length.text=""+milliSecondsToTimer(duration.toLong())
                content_image.setOnClickListener {
                    img_layout.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.ic_photo_green_24dp));
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val apkURI = FileProvider.getUriForFile(itemView.context, itemView.context.getPackageName() + ".provider", filepath)
                        intent.setDataAndType(apkURI, "video/*")
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    }else{
                        intent.setDataAndType(Uri.fromFile(filepath), "video/*")
                    }
                    itemView.context.startActivity(intent)
                }
            }else{
                vid_length.visibility=View.GONE
                play_imgae.visibility=View.GONE
                itemView.iv_image_content.load(filepath)
                content_image.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val apkURI = FileProvider.getUriForFile(itemView.context, itemView.context.getPackageName() + ".provider", filepath)
                        intent.setDataAndType(apkURI, "image/*")
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    }else{
                        intent.setDataAndType(Uri.fromFile(filepath), "image/*")
                    }
                    itemView.context.startActivity(intent)
                }
            }

            val imageLoader = Coil.imageLoader(itemView.context)



            content_cb.setOnCheckedChangeListener { compoundButton, b ->
                if (content_cb.isChecked){
                    listener(blogPost,position)
                }

            }
            }catch (e:Exception){
                e.printStackTrace()
            }
        }
        private fun milliSecondsToTimer(milliseconds: Long): Any {
            var finalTimerString = ""
            var secondsString = ""

            // Convert total duration into time
            val hours = (milliseconds / (1000 * 60 * 60)).toInt()
            val minutes = (milliseconds % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (milliseconds % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()

            // Add hours if there
            if (hours > 0) {
                finalTimerString = "$hours:"
            }

            // Prepending 0 to seconds if it is one digit
            secondsString = if (seconds < 10) {
                "0$seconds"
            } else {
                "" + seconds
            }
            finalTimerString = "$finalTimerString$minutes:$secondsString"

            // return timer string
            return finalTimerString
        }
    }
    inline fun Context.toast(message:String){
        Toast.makeText(this, message ,  Toast.LENGTH_LONG).show()
    }

}
