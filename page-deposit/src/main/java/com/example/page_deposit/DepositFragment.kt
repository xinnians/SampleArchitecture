package com.example.page_deposit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.AppInjector
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.repository.model.base.ViewState
import me.vponomarenko.injectionmanager.x.XInjectionManager

class DepositFragment : BaseFragment() {
    private var isHide = true
    private lateinit var mDepositViewModel: DepositViewModel

    private val navigation: DepositNavigation by lazy {
        XInjectionManager.findComponent<DepositNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_deposit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        (activity as BaseActivity?)?.showBottomNav()
//        mDepositViewModel = AppInjector.obtainViewModel(this)
        Log.e("[MainFragment]", "lotteryToken: ${getSharedViewModel().lotteryToken.value}")

    }
}