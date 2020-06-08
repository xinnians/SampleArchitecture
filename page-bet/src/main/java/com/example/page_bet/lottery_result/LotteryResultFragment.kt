package com.example.page_bet.lottery_result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseFragment
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LotteryResultFragment: BaseFragment() {

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_lottery_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    fun initView() {

    }

    fun initData() {

    }
}