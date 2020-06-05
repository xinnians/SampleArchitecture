package com.example.page_gift

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import me.vponomarenko.injectionmanager.x.XInjectionManager

class GiftFragment: BaseFragment() {
    private var isHide = true
    private lateinit var mGiftViewModel: GiftViewModel

    private val navigation: GiftNavigation by lazy {
        XInjectionManager.findComponent<GiftNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_gift, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        (activity as BaseActivity?)?.showBottomNav()
    }
}