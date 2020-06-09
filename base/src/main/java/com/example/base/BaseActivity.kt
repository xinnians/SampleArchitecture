package com.example.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.repository.room.LocalDatabase

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var mSharedViewModel: SharedViewModel
    private var db: LocalDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedViewModel = AppInjector.obtainViewModel(application)
    }

    abstract fun hideBottomNav()
    abstract fun showBottomNav()
}