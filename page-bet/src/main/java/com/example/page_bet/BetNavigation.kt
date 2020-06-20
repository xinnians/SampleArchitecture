package com.example.page_bet

import android.os.Bundle

interface BetNavigation {

    fun toBetPage(gameInfo: Bundle)
    fun toGameFavoritePage()
    fun goBackToBetMenuPage()
    fun toLotteryCenterPage()
    fun toShoppingCartPage(gameId: Bundle)
    fun goBackToBetPage()
    fun toLotteryResultPage(gameId: Bundle)
    fun fromResultToBetPage(gameInfo: Bundle)
}