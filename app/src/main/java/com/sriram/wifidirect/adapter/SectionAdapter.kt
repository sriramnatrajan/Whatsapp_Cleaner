package com.sriram.wifidirect.adapter


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

import com.sriram.wifidirect.R

private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)
val fragmentList:MutableList<Fragment> = ArrayList<Fragment>()
val fragmentTitleList:MutableList<String> = ArrayList<String>()
/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionAdapter(private val context: Context, fm: FragmentManager,internal var totalTabs: Int) :
        FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // return PlaceholderFragment.newInstance(position + 1)
        return fragmentList.get(position)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
    fun addFragment(fragment:Fragment,title:String){

        fragmentList.add(fragment)
        fragmentTitleList.add(title)

    }
}
