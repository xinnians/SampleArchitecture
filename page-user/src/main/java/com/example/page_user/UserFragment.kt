package com.example.page_user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import me.vponomarenko.injectionmanager.x.XInjectionManager

class UserFragment : BaseFragment() {
    private var isHide = true
    private lateinit var mUserViewModel: UserViewModel

    private val navigation: UserNavigation by lazy {
        XInjectionManager.findComponent<UserNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        (activity as BaseActivity?)?.showBottomNav()
    }
}