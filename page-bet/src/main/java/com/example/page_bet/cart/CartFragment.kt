package com.example.page_bet.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseFragment
import com.example.base.inflate
import com.example.page_bet.R
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment() {
    private lateinit var cartPagerAdapter: CartPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_cart)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartPagerAdapter = CartPagerAdapter(childFragmentManager)
        vpCartType.adapter = cartPagerAdapter
        tlCartType.setupWithViewPager(vpCartType)
    }


}