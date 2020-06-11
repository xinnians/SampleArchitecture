package com.example.samplearchitecture

import android.os.Bundle
import com.example.page_bet.BetNavigation
import com.example.page_deposit.DepositNavigation
import com.example.page_login.LoginNavigation
import com.example.page_main.MainNavigation

internal class Navigator : BaseNavigator(), BetNavigation, LoginNavigation, MainNavigation, DepositNavigation {
    override fun toBetPage(gameInfo: Bundle) {
        navController?.navigate(R.id.action_betMenuFragment_to_betFragment, gameInfo)
    }

    override fun toShoppingCartPage() {
        navController?.navigate(R.id.action_betFragment_to_cartFragment)
    }

    override fun toGameFavoritePage() {
        navController?.navigate(R.id.action_betMenuFragment_to_gameFavoriteFragment)
    }

    override fun goBackToBetMenuPage() {
        navController?.popBackStack()
    }

    override fun toLotteryCenterPage() {
        navController?.navigate(R.id.action_betMenuFragment_to_lotteryCenterFragment)
    }

    override fun toLotteryResultPage(gameId: Bundle) {
        navController?.navigate(R.id.action_lotteryCenterFragment_to_lotteryResultFragment, gameId)
    }

    override fun fromResultToBetPage(gameInfo: Bundle) {
        navController?.navigate(R.id.action_lotteryResultFragment_to_betFragment, gameInfo)
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
    }

    override fun forgetPasswordPage() {

    }

    override fun testPlay() {

    }

    override fun mainPage() {
        navController?.navigate(R.id.action_loginFragment_to_mainFragment)
    }

    // 首頁
    fun homePage() {
        navController?.navigate(R.id.action_global_mainFragment)
    }
    // 儲值頁面
    fun depositPage() {
        navController?.navigate(R.id.action_global_depositFragment)
    }
    // 使用者頁面
    fun userPage() {
        navController?.navigate(R.id.action_global_userFragment)
    }
    // 轉帳頁面
    fun transationPage() {
        navController?.navigate(R.id.action_global_transationFragment)
    }
    // 禮物頁面
    fun giftPage() {
        navController?.navigate(R.id.action_global_giftFragment)
    }
}