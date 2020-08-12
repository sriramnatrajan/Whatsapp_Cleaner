package com.sriram.wifidirect.adapter

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.sriram.wifidirect.R
import com.sriram.wifidirect.models.ImageModel
import com.sriram.wifidirect.utils.Utilities
import kotlinx.android.synthetic.main.docs_item_adapter.view.*
import java.io.File
import java.nio.file.Files
import java.text.DecimalFormat
import kotlin.random.Random


/**
 * Created by Sriram.N
 */


class DocsFileAdapter(val context: Context,
                      private val path: File,
                      private val items: List<ImageModel>,
                      private val listener: (ImageModel, Int) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val contextm: Context? = null
    var modelArrayList: ArrayList<ImageModel>? = null
    private val TAG: String = "AppDebug"
    val imageFile: String? = null

    private lateinit var utilval: Utilities
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {

        utilval = Utilities()

        val view = LayoutInflater.from(parent.context).inflate(R.layout.docs_item_adapter, parent, false)
        return BlogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {

            is BlogViewHolder -> {
                holder.bindItems(items.get(position), listener, path)
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
            Log.d("onclick", "of item")
        }
    }


    class BlogViewHolder
    constructor(
            itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        var modelArrayList: ArrayList<ImageModel>? = null
        val image_format = itemView.img_file_format
        val file_name = itemView.doc_file_name
        val file_size = itemView.docs_file_size
        val content_cb = itemView.docs_customCheckBox;

        @SuppressLint("DefaultLocale", "ResourceAsColor")
        fun bindItems(blogPost: ImageModel, listener: (ImageModel, Int) -> Unit, path: File) {
            val position = adapterPosition

            val photoUri: Uri = Uri.fromFile(File(blogPost.path))
            val filepath = File(path.toString() + "/" + blogPost.path)

            if (blogPost.path.endsWith(".pdf")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_pdf_icon));
            } else if (blogPost.path.endsWith(".txt")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_text_icon));
            } else if (blogPost.path.endsWith(".doc")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_doc_icon));
            } else if (blogPost.path.endsWith(".docx")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_doc_icon));
            } else if (blogPost.path.endsWith(".zip")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_zip_icon));
            } else if (blogPost.path.endsWith(".apk")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_apk_icon));
            } else if (blogPost.path.endsWith(".ppt")) {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_ppt_icon));
            } else {
                image_format.img_file_format.setImageDrawable(itemView.context.getDrawable(R.drawable.ic_unknown_help));
            }
            itemView.setOnClickListener {
                val flname: String = blogPost.path
                val extensionRemoved: String = flname.substring(flname.lastIndexOf("."));
                val formatname: String = extensionRemoved.replace(".", "")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                if (formatname != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val apkURI = FileProvider.getUriForFile(itemView.context, itemView.context.getPackageName() + ".provider", filepath)
                        Log.d("Format", extensionRemoved)
                        intent.setDataAndType(apkURI, "application/" + formatname)
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    } else {
                        intent.setDataAndType(Uri.fromFile(filepath), "application/" + formatname)
                    }
                    try {
                        itemView.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(itemView.context, "Couldn't find the app that opens this file" , Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }

                } else {
                    Toast.makeText(itemView.context, "File format not supported", Toast.LENGTH_SHORT).show()
                }


            }
            file_name.text = filepath.name
            file_size.text = getStringSizeLengthFile(filepath.length())
            content_cb.setOnCheckedChangeListener { compoundButton, b ->
                var pos = content_cb.getTag() as Int

            }
            content_cb.setOnCheckedChangeListener { compoundButton, b ->
                if (content_cb.isChecked) {
                    listener(blogPost, position)
                } else {

                }
            }

        }


        fun getStringSizeLengthFile(size: Long): String? {
            val df = DecimalFormat("0.00")
            val sizeKb = 1024.0f
            val sizeMb = sizeKb * sizeKb
            val sizeGb = sizeMb * sizeKb
            val sizeTerra = sizeGb * sizeKb
            if (size < sizeMb) return df.format(size / sizeKb.toDouble()) + "KB"
            else if (size < sizeGb) return df.format(size / sizeMb.toDouble()) + "MB"
            else if (size < sizeTerra) return df.format(size / sizeGb.toDouble()) + "GB"
            return ""
        }

    }

    inline fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}
