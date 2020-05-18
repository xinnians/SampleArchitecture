package com.example.samplearchitecture

import androidx.navigation.NavController

open class BaseNavigator {
    protected var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        navController = null
    }
}