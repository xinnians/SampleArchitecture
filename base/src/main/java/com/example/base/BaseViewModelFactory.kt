package com.example.base

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.ViewModelProvider
import com.example.repository.Repository

abstract class BaseViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    abstract var mApplication: Application?
    abstract var mRepository: Repository?
    abstract var mPreferences: SharedPreferencesProvider?
    abstract var mResource: Resources?

    abstract fun init(
        application: Application,
        repository: Repository,
        preferences: SharedPreferencesProvider,
        resource: Resources
    )

}