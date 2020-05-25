package com.example.page_main

import android.content.Context
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

    private fun init(){
        mMainViewModel = AppInjector.obtainViewModel(this)

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
