package com.example.page_main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.*
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MainFragment : BaseFragment() {
    private var isHide = true
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
        setListener()
    }

    private fun init() {
        mMainViewModel = AppInjector.obtainViewModel(this)
        btnUp.setOnClickListener { navigation.backToLoginPage() }
        btnDown.setOnClickListener { navigation.goToBetMenuPage() }
        Log.e("[MainFragment]", "lotteryToken: ${getSharedViewModel().lotteryToken.value}")

        mMainViewModel.getGameMenuResult(getSharedViewModel().lotteryToken.value.orEmpty())
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> Log.e("Ian", "ViewState.Success : ${state.data.data}")
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }

            }

        Log.e("[MainFragment]","lotteryToken: ${getSharedViewModel().lotteryToken.value}")
    }

    private fun setListener() {
        ctbTitleBar.let {
            it.money = "1,000,000.000"
            it.showTime = 5000
            it.showBack = true
            it.backListener(View.OnClickListener { navigation.backToLoginPage() }

            )
        }
    }
}
