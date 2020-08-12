package com.sriram.wifidirect.activities;

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import atyati.com.swayammsme.fragments.FILES_FILE_LOCATION
import com.google.android.material.snackbar.Snackbar
import com.sriram.sampletestapp.util.requestPermissionsCompat
import com.sriram.sampletestapp.util.shouldShowRequestPermissionRationaleCompat
import com.sriram.sampletestapp.util.showSnackbar
import com.sriram.wifidirect.R
import com.sriram.wifidirect.adapter.*
import com.sriram.wifidirect.adapter.small_adapters.ImageRowDisplayAdapter
import com.sriram.wifidirect.models.DocsModel
import com.sriram.wifidirect.models.ImageModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sample_layout.*
import java.io.File
import java.text.DecimalFormat


const val PERMISSION_REQUEST_STORAGE_READ = 0

const val PERMISSION_REQUEST_STORAGE_WRITE = 0
const val WHATSAPP_FILES_LOCATION = "/Whatsapp/Media/"

class MainActivity : AppCompatActivity(), ActivityCompat.OnRequestPermissionsResultCallback {
    var output: String? = null
    val adapter = DummyRecyclerAdapter()
    var df: DecimalFormat = DecimalFormat("#.##")
    private lateinit var layout: View
    lateinit var path:File
    lateinit var documents_file_recyclerview: RecyclerView
    lateinit var images_rcview_recyclerview_: RecyclerView
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        layout = findViewById(R.id.main_layout)
        readWhatsappData()
        scrollable_content.adapter = adapter
        val file = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_FILES_LOCATION)
        documents_file_recyclerview = findViewById(R.id.recyclerview_files_types) as RecyclerView
        images_rcview_recyclerview_ = findViewById(R.id.images_rcview) as RecyclerView
        setItems(image_item_card, "WhatsApp Images")
        readImages()
        setItems(document_item_card, "WhatsApp Documents")
        // Video
        setItems(videos_item_card, "WhatsApp Video")
        // getFileItemSize()
        getWhatsAppFolderSize()
        filesDocsRecyclerview()
    }


    private fun setItems(view: View, dir: String) {
        val file = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_FILES_LOCATION + "/" + dir)
        val file_whatsapp = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_FILES_LOCATION)


        val fileSize = dirSize(file)
        if (dir == "WhatsApp Images") {
            val d = fileSize as Double / (1024 * 1024)
            images_tv.text = "Images"
            images_size.text = "${df.format(d.toDouble())} MB"
            view.setOnClickListener {
                intent = Intent(this@MainActivity, DashBoardActivity::class.java)
                intent.putExtra("folder_name", "WhatsApp Images")
                startActivity(intent)
            }
        } else if (dir == "WhatsApp Documents") {
            val d = fileSize as Double / (1024 * 1024)
            documents_tv.text = "Documents"
            documents_size.text = "${df.format(d.toDouble())} MB"
            view.setOnClickListener {
                intent= Intent(this@MainActivity,DashBoardActivity::class.java)
                intent.putExtra("folder_name","WhatsApp Documents")
                startActivity(intent)
            }

        } else if (dir == "WhatsApp Video") {
            val d = fileSize as Double / (1024 * 1024)
            videos_tv.text = "Video"
            video_size.text = "${df.format(d.toDouble())} MB"

            view.setOnClickListener {
                intent = Intent(this@MainActivity, DashBoardActivity::class.java)
                intent.putExtra("folder_name", "WhatsApp Video")
                startActivity(intent)
            }
        }
    }
    fun addFragment(fragment: Fragment, title:String){

        fragmentList.add(fragment)
        fragmentTitleList.add(title)

    }

    fun fileItemSize(dir: String): String? {
        val file = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_FILES_LOCATION + "/" + dir)
        val fileSize = dirSize(file)
        val d = fileSize as Double / (1024 * 1024)
        if (dir == "WhatsApp Animated Gifs") {
            return getStringSizeLengthFile(fileSize)
        } else if (dir == "WhatsApp Audio") {
            return getStringSizeLengthFile(fileSize)
        } else if (dir == "WhatsApp Voice Notes") {
            return getStringSizeLengthFile(fileSize)
        } else if (dir == "WallPaper") {
            return getStringSizeLengthFile(fileSize)
        }
        return "0MB"
    }
   fun getWhatsAppFolderSize(){
       val file = File(Environment.getExternalStorageDirectory().toString() + WHATSAPP_FILES_LOCATION )
       val fileSize = dirSize(file)
       getStringSizeLengthFile(fileSize)
       tv_fileSize.text=getStringSizeLengthFile(fileSize)
       Log.d("Sriram ","Whatsapp Size   :"+fileSize)
    }


    private val mSmallFolderFilesname = listOf(
            fileItemSize("WhatsApp Audio")?.let { DocsModel(R.drawable.ic_action_voice, "Audio files", it) },
            fileItemSize("WhatsApp Voice Notes")?.let { DocsModel(R.drawable.ic_action_audio, "Voice Notes", it) },
            fileItemSize("WallPaper")?.let { DocsModel(R.drawable.ic_launcher_wallpaper, "Wallpaper", it) },
            fileItemSize("WhatsApp Animated Gifs")?.let { DocsModel(R.drawable.ic_launcher_gif, "GIF's Files", it) }
    )

    private fun WhatsappSize(dir: File): Double {
        var result: Double = 0.0
        if (dir.exists()) {
            val fileList = dir.listFiles()
            for (i in fileList.indices) {
                if (fileList[i].isDirectory()) {
                    result += dirSize(fileList[i])
                } else {
                    result += fileList[i].length()
                    val d = result as Double / (1024 * 1024)
                    Log.d("Whatsapp_Size", result.toByte().toString())
                    //fileSize.text="${d.toDouble()} MB"
                }
            }
            return result // return the file size
        }

        return 0.0
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

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_STORAGE_READ) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted.  Storage Accessible.
                layout.showSnackbar("Storage permission granted", Snackbar.LENGTH_SHORT)
                // startCamera()
            } else {
                //Storage Permission request was denied.
                layout.showSnackbar("Storage access permission denied", Snackbar.LENGTH_SHORT)
            }
        }
    }

    private fun readWhatsappData() =
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //    Toast.makeText(this@MainActivity, "Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                requestCameraPermission()
            }


    private fun requestCameraPermission() {
        // Permission has not been granted and must be requested.
        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // Display a SnackBar with a button to request the missing permission.

            layout.showSnackbar(
                    R.string.storage_access_required,
                    Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                        arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        PERMISSION_REQUEST_STORAGE_READ
                )
            }

        } else {
            layout.showSnackbar(R.string.camera_permission_not_available, Snackbar.LENGTH_SHORT)

            // Request the permission. The result will be received in onRequestPermissionResult().
            requestPermissionsCompat(
                    arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),

                    PERMISSION_REQUEST_STORAGE_READ
            )
        }
    }

    fun getStringSizeLengthFile(size: Double): String? {
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


    fun filesDocsRecyclerview() {
        documents_file_recyclerview.layoutManager = GridLayoutManager(applicationContext, 2)
        documents_file_recyclerview.adapter = Files_adapter(applicationContext, mSmallFolderFilesname as List<DocsModel>) { docsModel: DocsModel, i: Int ->
            Log.d("Sriram",docsModel.file_name);
            Toast.makeText(applicationContext,docsModel.file_name,Toast.LENGTH_SHORT).show()
            if (docsModel.file_name=="Audio files"){
                intent = Intent(this@MainActivity, AudioFilesActivity::class.java)
                intent.putExtra("file_name", "WhatsApp Audio")
                startActivity(intent)

            }else if (docsModel.file_name=="GIF's Files"){
                intent = Intent(this@MainActivity, DashBoardActivity::class.java)
                intent.putExtra("folder_name", "WhatsApp Animated Gifs")
                startActivity(intent)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    @SuppressLint("StaticFieldLeak")
    fun readImages() {
        var data:ArrayList<ImageModel> = ArrayList()
        val pathc=
                Environment.getExternalStorageDirectory().toString()+ FILES_FILE_LOCATION +"/" + "WhatsApp Images/"
        path = File(Environment.getExternalStorageDirectory().toString() + FILES_FILE_LOCATION + "/" + "WhatsApp Images/")
        var directory = path
        if (directory.exists())
        {
            val files = directory.listFiles()
            Log.d("Files", "Size: " + files.size);
            val paths = arrayOf<String>("")



            object: AsyncTask<Void, Void, Void>() {

                override fun onPreExecute() {
                    super.onPreExecute()
                }
                override fun doInBackground(vararg voids:Void): Void? {

                    for (i in 0 until files.size)
                    {

                     //   Log.d("Files", "FileName:" + files[i].getName())
                        if (files[i].getName().endsWith(".jpg") || files[i].getName().endsWith("gif"))
                        {
                            paths[0] = pathc +""+ files[i].getName()
                            val modelStatus = ImageModel(paths[0], files[i].getName())
                            data.add(modelStatus)
                        }
                    }
                    return null
                }

                override fun onPostExecute(result: Void?) {
                    super.onPostExecute(result)
                    if (data.size==0){
                        images_rcview_recyclerview_.visibility=View.GONE
                    }
                    images_rcview_recyclerview_.layoutManager= GridLayoutManager(applicationContext,1,GridLayoutManager.HORIZONTAL ,false)
                    images_rcview_recyclerview_.adapter= ImageRowDisplayAdapter(applicationContext,path,data){ contentModel:ImageModel, i ->
                       images_rcview_recyclerview_.setHasFixedSize(true)
                        intent = Intent(this@MainActivity, DashBoardActivity::class.java)
                        intent.putExtra("folder_name", "WhatsApp Images")
                        startActivity(intent)
                    }
                }
            }.execute()

        }
    }


}
