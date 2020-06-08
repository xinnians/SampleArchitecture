package com.example.page_transation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import me.vponomarenko.injectionmanager.x.XInjectionManager

class TransationFragment : BaseFragment() {
    private var isHide = true
    private lateinit var mTransationViewModel: TransationViewModel

    private val navigation: TransationNavigation by lazy {
        XInjectionManager.findComponent<TransationNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_transation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        (activity as BaseActivity?)?.showBottomNav()
    }
}