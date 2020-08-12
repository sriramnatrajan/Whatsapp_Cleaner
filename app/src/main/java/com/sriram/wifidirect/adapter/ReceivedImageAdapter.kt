package com.sriram.wifidirect.adapter



import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.bumptech.glide.Glide
import com.sriram.wifidirect.R
import com.sriram.wifidirect.models.ImageModel
import com.sriram.wifidirect.utils.Utilities
import kotlinx.android.synthetic.main.image_adapter.view.*
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.random.Random


/**
 * Created by Sriram.N
 */


class ReceivedImageAdapter(  val context: Context,
                           private val path:File,
                           private val items:List<ImageModel>,
                           private val listener:(ImageModel,Int)->Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private val contextm: Context? = null
    var modelArrayList: ArrayList<ImageModel>? = null
    private val TAG: String = "AppDebug"
      val imageFile:String?=null

      private lateinit var utilval: Utilities
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {

        utilval= Utilities()

        val view= LayoutInflater.from(parent.context).inflate(R.layout.image_adapter, parent, false)
            return BlogViewHolder(view)
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
        var modelArrayList: ArrayList<ImageModel>? = null
        val img_layout=itemView.image_layout
        val content_image= itemView.iv_image_content
        val content_cb=itemView.cb_mark_image
        val play_image=itemView.iv_play_image
        val vid_length=itemView.vid_length
        @SuppressLint("DefaultLocale")
        fun bindItems(blogPost: ImageModel, listener: (ImageModel, Int) -> Unit, path: File) {
            val position=adapterPosition

            val photoUri: Uri = Uri.fromFile(File(blogPost.path))
            val filepath=File(path.toString()+"/"+blogPost.path)
            vid_length.text=filepath.length().toString()

            if (blogPost.path.endsWith(".mp4")){

                img_layout.visibility=View.VISIBLE
                play_image.visibility=View.GONE
                Glide.with(itemView.context)
                        .load(filepath)
                        .into(content_image);
                val mp: MediaPlayer = MediaPlayer.create(itemView.context, Uri.parse(filepath.toString()))
                val duration = mp.duration
                mp.release()
                val format_tv= java.lang.String.format("%tT",
                        TimeUnit.MILLISECONDS.toMinutes(duration.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(duration.toLong()) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration.toLong()))
                )

                        //(itemView.context as Utilities).milliSecondsToTimer(duration.toLong())
                 vid_length.text=""+milliSecondsToTimer(duration.toLong())
                    play_image.setOnClickListener {
                    img_layout.iv_image_content.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.ic_photo_green_24dp));
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION )
                    intent.setDataAndType(Uri.fromFile(filepath), "video/*")
                    itemView.context.startActivity(intent)

                }
            }else{
                vid_length.visibility=View.GONE
                img_layout.iv_image_content.setBackgroundDrawable(itemView.context.getDrawable(R.drawable.ic_photo_green_24dp));
                itemView.iv_image_content.load(filepath)
            }

            itemView.setOnClickListener {
                listener(blogPost,position)
            }

            content_cb.setOnCheckedChangeListener { compoundButton, b ->
                var pos = content_cb.getTag() as Int

            }
            content_cb.setOnCheckedChangeListener { compoundButton, b ->
                if (content_cb.isChecked){
                    listener(blogPost,position)
                }else{

                }
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
