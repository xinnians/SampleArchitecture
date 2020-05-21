package com.example.samplearchitecture

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.base.BaseViewModelFactory
import com.example.base.ResourceProvider
import com.example.base.SharedPreferencesProvider
import com.example.base.SharedViewModel
import com.example.page_bet.BetViewModel
import com.example.page_login.LoginViewModel
import com.example.page_main.MainViewModel
import com.example.repository.Repository

class ViewModelFactory(
    override var mApplication: Application? = null,
    override var mRepository: Repository? = null,
    override var mPreferences: SharedPreferencesProvider? = null,
    override var mResource: ResourceProvider? = null
) : BaseViewModelFactory() {
    override fun init(
        application: Application,
        repository: Repository,
        preferences: SharedPreferencesProvider,
        resource: ResourceProvider
    ) {
        mApplication = application
        mRepository = repository
        mPreferences = preferences
        mResource = resource
        Log.e("[ViewModelFactory]","init call.")
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(BetViewModel::class.java) -> BetViewModel(repository = mRepository!!)
                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(repository = mRepository!!)
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(repository = mRepository!!)
                isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel(repository = mRepository!!)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            } as T
        }
    }

}