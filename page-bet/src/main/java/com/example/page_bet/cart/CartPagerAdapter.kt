package com.example.page_bet.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CartPagerAdapter(private val fragmentManager: FragmentManager, private val gameId: List<Int>, private val gameName: List<String>):
    FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return CartListFragment().apply {
            arguments = Bundle().apply {
                putInt("gameId", gameId[position])
            }
        }
    }

    override fun getCount() = gameId.size

    override fun getPageTitle(position: Int): CharSequence? {
        return gameName[position]
    }
}