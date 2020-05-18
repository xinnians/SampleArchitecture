package com.example.samplearchitecture

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.base.AppInjector.repository
import com.example.base.BaseViewModelFactory
import com.example.base.ResourceProvider
import com.example.base.SharedPreferencesProvider
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
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return with(modelClass) {
            when {
                isAssignableFrom(BetViewModel::class.java) -> BetViewModel(repository = repository)
                isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel()
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel()
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            } as T
        }
    }

}