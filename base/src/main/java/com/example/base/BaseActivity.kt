package com.example.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mSharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedViewModel = AppInjector.obtainViewModel(application)
    }

    abstract fun hideBottomNav()
    abstract fun showBottomNav()
}