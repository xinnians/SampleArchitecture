package com.example.page_login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_login.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class LoginFragment : Fragment() {

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

        btnUp.setOnClickListener { navigation.openLoginUp() }
        btnDown.setOnClickListener { navigation.openLoginDown() }
    }
}