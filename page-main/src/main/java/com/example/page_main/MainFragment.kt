package com.example.page_main

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.onClick
import kotlinx.android.synthetic.main.tool_bar.*
import com.example.base.observeNotNull
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
        tvMoney.let {
            it.text = "1,000"
            it.transformationMethod = PasswordTransformationMethod.getInstance()
        }

        ivShowMoney.onClick {
            if (isHide) {
                isHide = false
                ivShowMoney.setImageDrawable(ContextCompat.getDrawable(requireActivity(), R.drawable.open_eye))
                tvMoney.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                isHide = true
                ivShowMoney.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.close_eye))
                tvMoney.let {
                    it.maxEms = 6
                    it.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
        }
    }
}
