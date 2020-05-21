package com.example.samplearchitecture

import com.example.page_bet.BetNavigation
import com.example.page_login.LoginNavigation
import com.example.page_main.MainNavigation

internal class Navigator: BaseNavigator(), BetNavigation,LoginNavigation,MainNavigation {
    override fun openBetUp() {
        navController?.popBackStack()
    }

    override fun openBetDown() {
    }

    override fun toGameFavoritePage() {
        navController?.navigate(R.id.action_betMenuFragment_to_gameFavoriteFragment)
    }

    override fun goBackToBetMenuPage() {
        navController?.popBackStack()
    }

    override fun openLoginUp() {
//        navController?.popBackStack()
    }

    override fun openLoginDown() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }

    override fun backToLoginPage() {
        navController?.popBackStack()
    }

    override fun goToBetMenuPage() {
        navController?.navigate(R.id.action_mainFragment_to_betMenuFragment)
    }
}