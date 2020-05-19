package com.example.samplearchitecture.application

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.example.base.AppInjector
import com.example.samplearchitecture.Navigator
import com.example.samplearchitecture.ViewModelFactory
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MyApplication : Application(),ViewModelStoreOwner {

    //TODO tip：可借助 Application 来管理一个应用级 的 SharedViewModel，
    // 实现全应用范围内的 生命周期安全 且 事件源可追溯的 视图控制器 事件通知。
    private lateinit var mAppViewModelStore: ViewModelStore

    override fun onCreate() {
        super.onCreate()

        mAppViewModelStore = ViewModelStore()

        AppInjector.init(this, ViewModelFactory())

        XInjectionManager.bindComponentToCustomLifecycle(object : IHasComponent<Navigator> {
            override fun getComponent(): Navigator {
                return Navigator()
            }

        })
    }

    override fun getViewModelStore(): ViewModelStore = mAppViewModelStore
}