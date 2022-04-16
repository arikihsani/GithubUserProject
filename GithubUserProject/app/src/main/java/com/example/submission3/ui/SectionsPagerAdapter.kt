package com.example.submission3.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = ""

    override fun createFragment(position: Int): Fragment {
        return FollowFragment.newInstance(position)
    }

    override fun getItemCount(): Int {
        return 2
    }

}