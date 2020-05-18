package com.example.samplearchitecture.application

import android.app.Application
import com.example.base.AppInjector
import com.example.samplearchitecture.Navigator
import com.example.samplearchitecture.ViewModelFactory
import me.vponomarenko.injectionmanager.IHasComponent
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this, ViewModelFactory())

        XInjectionManager.bindComponentToCustomLifecycle(object : IHasComponent<Navigator> {
            override fun getComponent(): Navigator {
                return Navigator()
            }

        })
    }
}