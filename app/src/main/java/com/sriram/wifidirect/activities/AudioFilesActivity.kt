package com.sriram.wifidirect.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import atyati.com.swayammsme.fragments.ReceivedFilesFragment
import atyati.com.swayammsme.fragments.SentFilesFragment
import com.sriram.wifidirect.R
import java.io.File


class AudioFilesActivity : AppCompatActivity() {
    lateinit var docs_recyclerview_: RecyclerView
    lateinit var path: File
    val fileSizeArrayList: ArrayList<Long> = ArrayList()
    var file_type:String = "";
    var textView_title:TextView?=null;
    var iv_back:ImageView?=null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audios)

        file_type=intent.getStringExtra("file_name")
        openStageOneFrag()
        textView_title=findViewById(R.id.title) as TextView
        textView_title!!.text=file_type;
        iv_back?.setOnClickListener {
            finish()
        }
    }
      fun openStageOneFrag() {
        supportFragmentManager.popBackStack(getFragmentName(SentFilesFragment()), 1)
        val stageOneFrag = SentFilesFragment()
        addFragment(stageOneFrag, false, getFragmentName(stageOneFrag))
        val result = Bundle()
        result!!.putString("folder_name",file_type);
        stageOneFrag.arguments=result
    }

    fun addFragment(fragment: Fragment?, backStack: Boolean, tag: String?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.add(R.id.frameLayout, fragment!!)
        if (backStack) {
            fragmentTransaction.addToBackStack(tag)
        }
        fragmentTransaction.commitAllowingStateLoss()
    }

    fun getFragmentName(fragment: Fragment): String? {
        return fragment.javaClass.name.toString()
    }
}