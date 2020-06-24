package com.example.base

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.example.repository.room.LocalDatabase


abstract class  BaseActivity : AppCompatActivity() {

    private var mKeyDownListenerList: ArrayList<OnKeyDownListener>? = arrayListOf()

    private lateinit var mSharedViewModel: SharedViewModel
    private var db: LocalDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mSharedViewModel = AppInjector.obtainViewModel(application)

        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)

        dm.apply {
            mSharedViewModel.isBigScreen.value = this.heightPixels/this.widthPixels > 16/9
        }
//        Log.e("Ian","widthPixels:${dm.widthPixels}, heightPixels:${dm.heightPixels}, density:${dm.density}, densityDpi:${dm.densityDpi}, scaledDensity:${dm.scaledDensity}, xdpi:${dm.xdpi}, ydpi:${dm.ydpi}")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        mKeyDownListenerList?.forEach {
            it.onKeyDown(keyCode)
        }
        return super.onKeyDown(keyCode, event)
    }

    abstract fun hideBottomNav()
    abstract fun showBottomNav()

    interface OnKeyDownListener {
        fun onKeyDown(keyCode: Int)
    }

    fun addKeyDownListener(listener: OnKeyDownListener) {
        mKeyDownListenerList?.add(listener)
    }

    fun removeKeyDownListener(listener: OnKeyDownListener) {
        mKeyDownListenerList?.apply {
            if (this.contains(listener)) this.remove(listener)
        }
    }
}