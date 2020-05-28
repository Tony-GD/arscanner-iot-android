package com.griddynamics.connectedapps.ui.home

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


typealias Callback = () -> Unit

class TabAdapter internal constructor(fragmentManager: Fragment) : FragmentStateAdapter(
    fragmentManager
) {
    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val fragmentTitleList: MutableList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    fun getTabTitle(position: Int) : String {
        return fragmentTitleList[position]
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}