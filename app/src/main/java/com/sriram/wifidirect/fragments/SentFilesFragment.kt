package atyati.com.swayammsme.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sriram.wifidirect.R
import com.sriram.wifidirect.adapter.ContentRecyclerAdapter
import com.sriram.wifidirect.adapter.DocsFileAdapter
import com.sriram.wifidirect.models.ImageModel
import com.sriram.wifidirect.utils.CustomCheckBox
import kotlinx.android.synthetic.main.fragment_received_files.*
import kotlinx.android.synthetic.main.fragment_sent_files.*

import java.io.File
import java.text.DecimalFormat

const val FILES_FILE_LOCATION = "/Whatsapp/Media/"
const val SENT_FILES_FOLDER = "/WhatsApp Images/Sent/"

class SentFilesFragment : Fragment() {
    val fileSizeArrayList: ArrayList<Long> = ArrayList()
    var deleted_item_sent: TextView? = null
    private lateinit var contentRecyclerAdapter: ContentRecyclerAdapter
    lateinit var sentFiles_recyclerview_: RecyclerView
    lateinit var path: File
    lateinit var mCustomCheckBox: CustomCheckBox
    var data: ArrayList<ImageModel> = ArrayList()
    var folder_name: String? = null;
    override fun onAttach(context: Context) {
        super.onAttach(context)
        folder_name = arguments!!.getString("folder_name")
        Log.d("Sriram", "Folder name=== " + folder_name)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_sent_files, container, false)
        sentFiles_recyclerview_ =
                view.findViewById(R.id.files_recyclerview) as RecyclerView
        deleted_item_sent = view.findViewById(R.id.deleted_item_sent) as TextView
        mCustomCheckBox = view.findViewById(R.id.cb_received_total_file) as CustomCheckBox
        fileSizeArrayList.clear()
        data.clear()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readImages()
        var df: DecimalFormat = DecimalFormat("#.##")
        path = File(Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name + "/Sent")
        val fileSize = dirSize(path)
        val d = fileSize as Double / (1024 * 1024)
        tv_total_file_size_sent.text = getStringSizeLengthFile(fileSize.toLong())
        Log.d("Sriram File Size", getStringSizeLengthFile(fileSize.toLong()))
        mCustomCheckBox.setOnCheckedChangeListener { compoundButton, b ->

        }
        deleted_item_sent?.setOnClickListener {

            var toast = Toast.makeText(activity, "${fileSizeArrayList.size}", Toast.LENGTH_SHORT)
            toast.show()
        }
        handleButtonColor()
    }


    @SuppressLint("StaticFieldLeak")
    fun readImages() {

        val pathc = Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name + "/Sent"
        val path_recv = Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name

        path = File(Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name + "/Sent")
        var directory = path
        if (directory.exists()) {

            val files = directory.listFiles()
            Log.d("Files", "Size: " + files.size);
            val paths = arrayOf<String>("")
            val path2= arrayOf("")
            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {

                    for (i in 0 until files.size) {
                        Log.d("Files", "FileName:" + files[i].getName())
                        Log.d("Files", "FileName:" + files[i].getName())
                        if (folder_name.equals("WhatsApp Images")) {
                            if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith("PNG")) {
                                paths[0] = pathc + "" + files[i].getName()
                                val modelStatus = ImageModel(paths[0], files[i].getName())
                                data.add(modelStatus)
                            }
                        } else if (folder_name.equals("WhatsApp Video")) {
                            if (files[i].getName().endsWith(".mp4")) {
                                paths[0] = pathc + "" + files[i].getName()
                                val modelStatus = ImageModel(paths[0], files[i].getName())
                                data.add(modelStatus)
                            }
                        } else if (folder_name.equals("WhatsApp Documents")) {
                            paths[0] = pathc + "" + files[i].getName()
                            val iFile: Boolean = files[i].isFile
                            if (iFile) {
                                val modelStatus = ImageModel(paths[0], files[i].getName())
                                data.add(modelStatus)
                            }
                        }  else if (folder_name.equals("WhatsApp Animated Gifs")) {
                            if (files[i].getName().endsWith(".mp4")) {
                                paths[0] = pathc + "" + files[i].getName()
                                path2[0]=path_recv +"" +files[i].getName()
                                val modelStatus = ImageModel(paths[0], files[i].getName())
                                data.add(modelStatus)

                            }
                        }
                    }
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)

                    if (data.size == 0) {
                        sentFiles_recyclerview_.visibility = View.GONE
                    } else {
                        if (folder_name.equals("WhatsApp Documents")) {
                            sentFiles_recyclerview_.layoutManager = LinearLayoutManager(activity)
                            sentFiles_recyclerview_.adapter = DocsFileAdapter(requireContext(), path, data) { contentModel: ImageModel, i ->

                            }
                        } else {
                            sentFiles_recyclerview_.layoutManager = GridLayoutManager(activity, 3)
                            sentFiles_recyclerview_.adapter = ContentRecyclerAdapter(requireContext(), path, data) { contentModel: ImageModel, i ->

                                val f = File(pathc + "/" + contentModel.path)
                                if (f.exists()) {
                                    getStringSizeLengthFile(f.length())
                                }
                                val fileSize = getStringSizeLengthFile(f.length())
                                Log.d("Sixx", fileSize)
                                if (fileSize != null) {
                                    if (fileSizeArrayList.contains(f.length())) {
                                        Log.d("Sriram", "File exists")
                                    } else {
                                        fileSizeArrayList.add(f.length())
                                    }
                                }
                            }
                        }
                    }

                }
            }.execute()
            if (mCustomCheckBox.isChecked) {

                // Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        handleButtonColor()

    }

    fun handleButtonColor() {
        val mHandler = Handler()
        val runnable: Runnable = object : Runnable {
            var count = 0

            @SuppressLint("SetTextI18n")
            override fun run() {
                count++
                if (fileSizeArrayList.size > 0) {

                    deleted_item_sent?.setTextColor(Color.parseColor("#42A5F5"))
                }
                deleted_item_sent?.text = "Delete selected items (" + getStringSizeLengthFile(fileSizeArrayList.sum()) + ")"
                mHandler.postDelayed(this, 10) // five second in ms
            }
        }
        mHandler.postDelayed(runnable, 10)
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

    private fun dirSize(dir: File): Double {
        var result: Double = 0.0
        if (dir.exists()) {
            val fileList = dir.listFiles()
            for (i in fileList.indices) {
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i])
                } else {
                    result += fileList[i].length()
                    val d = result as Double / (1024 * 1024)
                    Log.d("Content_item", result.toByte().toString())
                }
            }
            return result
        }
        return 0.0
    }


    override fun onDestroy() {
        super.onDestroy()
        fileSizeArrayList.clear()
        data.clear()
    }

    override fun onDetach() {
        super.onDetach()
        fileSizeArrayList.clear()
        data.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fileSizeArrayList.clear()
        data.clear()
    }
}


