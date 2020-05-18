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

    override fun openLoginUp() {
//        navController?.popBackStack()
    }

    override fun openLoginDown() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }

    override fun openMainUp() {
        navController?.popBackStack()
    }

    override fun openMainDown() {
        navController?.navigate(R.id.action_mainFragment_to_betFragment)
    }
}