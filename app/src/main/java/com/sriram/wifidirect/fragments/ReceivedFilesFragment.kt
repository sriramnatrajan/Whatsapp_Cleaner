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
import com.sriram.wifidirect.adapter.ReceivedImageAdapter
import com.sriram.wifidirect.models.ImageModel
import kotlinx.android.synthetic.main.fragment_received_files.*
import java.io.File
import java.text.DecimalFormat


class ReceivedFilesFragment : Fragment() {
    var deleted_item:TextView? = null
    val fileSizeArrayList: ArrayList<Long> = ArrayList()
    var imagePathList:ArrayList<ImageModel> = ArrayList()
     lateinit var imageModel:ImageModel
    private lateinit var contentRecyclerAdapter: ReceivedImageAdapter
    lateinit var received_files_recyclerview: RecyclerView
    lateinit var path: File
    var data: ArrayList<ImageModel> = ArrayList()
    var folder_name: String?=null;
    private  lateinit var mcontext:Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
          folder_name  = arguments!!.getString("folder_name")
        Log.d("Sriram","Folder name=== "+folder_name)
    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater!!.inflate(R.layout.fragment_received_files, container, false)

        received_files_recyclerview = view.findViewById(R.id.received_files_recyclerview) as RecyclerView
        deleted_item=view.findViewById(R.id.deleted_item) as TextView
        fileSizeArrayList.clear()
        data.clear()
        fileSizeArrayList.clear()
        imagePathList.clear()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        readImages()
        var df: DecimalFormat = DecimalFormat("#.##")
        val fileSize = dirSize(path)
        val d = fileSize as Double / (1024 * 1024)
        tv_total_file_size.text = "${df.format(d)} MB"
        cb_received_total_file.setOnCheckedChangeListener { compoundButton, b ->
            if (cb_received_total_file.isChecked) {
                Toast.makeText(activity, "clicked", Toast.LENGTH_SHORT).show()
            }
        }

        deleted_item?.setOnClickListener {

            var toast = Toast.makeText(activity, "${fileSizeArrayList.size}", Toast.LENGTH_SHORT)
            toast.show()
        }
        handleButtonColor()
    }

    @SuppressLint("StaticFieldLeak")
    fun readImages() {

        val pathc = Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name
        path = File(Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + folder_name)
        var directory = path
        if (directory.exists()) {
            val files = directory.listFiles()
            Log.d("Files", "Size: " + files.size);
            val paths = arrayOf<String>("")

            object : AsyncTask<Void, Void, Void>() {
                override fun doInBackground(vararg voids: Void): Void? {
                    for (i in 0 until files.size) {
                        Log.d("Files", "FileName:" + files[i].getName())
                        Log.d("Files", "FileName:" + files[i].getName())
                        if (folder_name.equals("WhatsApp Images")) {
                            if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith("gif")) {
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

                        } else if (folder_name.equals("WhatsApp Animated Gifs")) {
                            if (files[i].getName().endsWith(".mp4")) {
                                paths[0] = pathc + "" + files[i].getName()

                                val modelStatus = ImageModel(paths[0], files[i].getName())
                                data.add(modelStatus)

                            }
                        }
                    }
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    if (folder_name.equals("WhatsApp Documents")){
                        received_files_recyclerview.layoutManager= LinearLayoutManager(activity)
                        received_files_recyclerview.adapter= DocsFileAdapter(requireContext(),path,data){ contentModel: ImageModel, i ->

                        }
                    }else{
                        received_files_recyclerview.layoutManager= GridLayoutManager(activity,3)
                        received_files_recyclerview.adapter=ContentRecyclerAdapter(requireContext(),path,data){ contentModel:ImageModel, i ->
                            /*val toast=Toast.makeText(activity,"${contentModel.path}",Toast.LENGTH_SHORT)
                            toast.show()*/
                            val f = File(pathc + "/" + contentModel.path)
                            if (f.exists()) {
                                getStringSizeLengthFile(f.length())
                            }
                            val fileSize=getStringSizeLengthFile(f.length())
                            Log.d("Sixx",fileSize)
                            if (fileSize != null) {
                                if (fileSizeArrayList.contains(f.length())){
                                    Log.d("Sriram","File exists")
                                }else{
                                    fileSizeArrayList.add(f.length())
                                }
                        }
                    /*   received_files_recyclerview.layoutManager = GridLayoutManager(activity, 3)
                    received_files_recyclerview.adapter = ContentRecyclerAdapter(requireContext(), path, data) { contentModel: ImageModel, i ->
*/


                    }

                        /*val toast = Toast.makeText(activity, "${contentModel.path}", Toast.LENGTH_SHORT)
                        toast.show()*/
                        deleted_item?.setOnClickListener {
                            for (i in 0 until imagePathList.size) {
                                Log.d("Sriram",imagePathList[i].toString())
                            }

                        }
                    }
                }
            }.execute()
        }
    }

    override fun onResume() {
        super.onResume()
        handleButtonColor()
    }

    fun handleButtonColor(){
       val mHandler = Handler()
          val runnable: Runnable = object : Runnable {
              var count = 0
              @SuppressLint("SetTextI18n")
              override fun run() {
                  count++
                  if (fileSizeArrayList.size>0){

                      deleted_item?.setTextColor(Color.parseColor("#42A5F5"))
                  }
                  deleted_item?.text = "Delete selected items ("+getStringSizeLengthFile(fileSizeArrayList.sum())+")"
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
        if (size < sizeMb) return df.format(size / sizeKb.toDouble()) +"KB"
        else if (size < sizeGb) return df.format(size / sizeMb.toDouble()) +"MB"
        else if (size < sizeTerra) return df.format(size / sizeGb.toDouble()) +"GB"
        return ""
    }

    private fun dirSize(dir: File): Double {
        var result: Double = 0.0
        if (dir.exists()) {
            val fileList = dir.listFiles()
            for (i in fileList.indices) {
                // Recursive call if it's a directory
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i])
                } else {
                    result += fileList[i].length()
                    val d = result as Double / (1024 * 1024)
                    Log.d("Content_item", result.toByte().toString())
                }
            }
            return result // return the file size
        }
        return 0.0
    }

    override fun onDestroy() {
        super.onDestroy()
        fileSizeArrayList.clear()
        imagePathList.clear()
        data.clear()
    }

    override fun onDetach() {
        super.onDetach()
        fileSizeArrayList.clear()
        imagePathList.clear()
        data.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fileSizeArrayList.clear()
        imagePathList.clear()
        data.clear()
    }

}