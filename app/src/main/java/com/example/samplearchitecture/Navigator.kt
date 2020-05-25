package com.example.samplearchitecture

import android.util.Log
import com.example.page_bet.BetNavigation
import com.example.page_login.LoginNavigation
import com.example.page_main.MainNavigation

internal class Navigator: BaseNavigator(), BetNavigation,LoginNavigation,MainNavigation {
    override fun openBetUp() {
        navController?.popBackStack()
    }

    override fun openBetDown() {

    }

    override fun registerPage() {
        navController?.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun loginPage() {
        navController?.popBackStack()
    }

    override fun forgetPasswordPage() {

    }

    override fun testPlay() {

    }

    override fun mainPage() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }


}