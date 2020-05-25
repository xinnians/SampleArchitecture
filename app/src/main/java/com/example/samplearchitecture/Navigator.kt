package com.example.samplearchitecture

import android.os.Bundle
import com.example.page_bet.BetNavigation
import com.example.page_login.LoginNavigation
import com.example.page_main.MainNavigation

internal class Navigator : BaseNavigator(), BetNavigation, LoginNavigation, MainNavigation {
    override fun toBetPage(gameInfo: Bundle) {
        navController?.navigate(R.id.action_betMenuFragment_to_betFragment, gameInfo)
    }

    override fun toGameFavoritePage() {
        navController?.navigate(R.id.action_betMenuFragment_to_gameFavoriteFragment)
    }

    override fun goBackToBetMenuPage() {
        navController?.popBackStack()
    }

    override fun registerPage() {
        navController?.navigate(R.id.action_loginFragment_to_registerFragment)
    }

    override fun loginPage() {
        navController?.popBackStack()
    }

    override fun backToLoginPage() {
        navController?.popBackStack()
    }

    override fun goToBetMenuPage() {
        navController?.navigate(R.id.action_mainFragment_to_betMenuFragment)

    override fun forgetPasswordPage() {

    }

    override fun testPlay() {

    }

    override fun mainPage() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }


}