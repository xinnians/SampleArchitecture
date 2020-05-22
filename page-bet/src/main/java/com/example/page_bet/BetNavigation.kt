package com.example.page_bet

import android.os.Bundle

interface BetNavigation {

    fun toBetPage(gameInfo: Bundle)
    fun toGameFavoritePage()
    fun goBackToBetMenuPage()
}