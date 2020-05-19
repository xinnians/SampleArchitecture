package com.example.page_login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_login.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LoginFragment : BaseFragment() {

    private lateinit var mViewModel: LoginViewModel

    private val navigation: LoginNavigation by lazy {
        XInjectionManager.findComponent<LoginNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init(){
        mViewModel = AppInjector.obtainViewModel(this)
        btnUp.setOnClickListener { navigation.openLoginUp() }
        btnDown.setOnClickListener { navigation.openLoginDown() }

        mViewModel.getLoginResult().observeNotNull(this){ state ->
            when(state) {
                is ViewState.Success -> {
                    Log.e("Ian","ViewState.Success : ${state.data.token}")
                    getSharedViewModel().lotteryToken.postValue(state.data.token)
                }
                is ViewState.Loading -> Log.e("Ian","ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }

    }
}