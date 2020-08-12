package com.sriram.wifidirect.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import atyati.com.swayammsme.fragments.ReceivedFilesFragment
import atyati.com.swayammsme.fragments.SentFilesFragment
import com.google.android.material.tabs.TabLayout
import com.sriram.wifidirect.R


/**
 * Created by Sriram.N
 */

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)
val fragmentList:MutableList<Fragment> = ArrayList<Fragment>()
val fragmentTitleList:MutableList<String> = ArrayList<String>()
var folder_name:String = "";
@Suppress("UNREACHABLE_CODE")
class DashBoardActivity : AppCompatActivity()  {
    var tabLayout: TabLayout? = null
    var viewPager: ViewPager? = null
    var back_ivv:ImageView?=null
    private lateinit var layout: View
    var tv_title:TextView?=null
    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)
          folder_name = intent.getStringExtra("folder_name");

        tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        viewPager = findViewById<ViewPager>(R.id.viewPager)

        val image_view = findViewById(R.id.back_imageview) as ImageView
        tv_title=findViewById(R.id.title) as TextView
        image_view.setOnClickListener {
            finish()
            // your code here
        }
        if ( folder_name.equals("WhatsApp Images")){
            tv_title!!.text="Images"
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Received Images"))
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Sent Images"))
        }else if (folder_name.equals("WhatsApp Video")){
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Received Videos"))
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Sent Videos"))
            tv_title!!.text="Videos"
        }else if (folder_name.equals("WhatsApp Documents")){
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Received files"))
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Sent files"))
            tv_title!!.text="Documents"
        }else if (folder_name.equals("WhatsApp Animated Gifs")){
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Received files"))
            tabLayout!!.addTab(tabLayout!!.newTab().setText("Sent files"))
            tv_title!!.text="Gifs"
        }

        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        setStatePageAdapter()
    }



    private fun setStatePageAdapter() {
        val adapter = SectionAdapter(this, supportFragmentManager, tabLayout!!.tabCount)
        adapter.addFragment(ReceivedFilesFragment(), "")
        adapter.addFragment(SentFilesFragment(), "")

        viewPager!!.adapter = adapter

        viewPager!!.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))

        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager!!.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        fragmentList.clear()
        finish()
    }


/**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionAdapter(private val context: Context, fm: FragmentManager, internal var totalTabs: Int) :
            FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {

            return fragmentList.get(position)
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context.resources.getString(TAB_TITLES[position])
        }

        override fun getCount(): Int {
            return 2
        }
        fun addFragment(fragment: Fragment, title:String){
            val bundle = Bundle()

            bundle.putString("folder_name", folder_name)
            fragment.arguments = bundle;
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }
    }
}