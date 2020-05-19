package com.example.page_main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import kotlinx.android.synthetic.main.fragment_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MainFragment : BaseFragment() {

    private lateinit var mMainViewModel: MainViewModel

    private val navigation: MainNavigation by lazy {
        XInjectionManager.findComponent<MainNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        mMainViewModel = AppInjector.obtainViewModel(this)
        btnUp.setOnClickListener { navigation.openMainUp() }
        btnDown.setOnClickListener { navigation.openMainDown() }
//        getSharedViewModel().lotteryToken.observeNotNull(this){
//
//        }
        Log.e("[MainFragment]","lotteryToken: ${getSharedViewModel().lotteryToken.value}")


    }
}