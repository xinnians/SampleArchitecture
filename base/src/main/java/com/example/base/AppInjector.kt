package com.example.base

import android.app.Activity
import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.example.repository.Repository
import com.example.repository.api.BaseAPI
import com.example.repository.api.SampleAPI
import com.example.repository.room.LocalDatabase

object AppInjector {

    private lateinit var application: Application
    private lateinit var viewModelFactory: BaseViewModelFactory
    private lateinit var repository: Repository
    private lateinit var preferences: SharedPreferencesProvider
    private lateinit var resource: ResourceProvider
    private lateinit var localDb: LocalDatabase

    fun init(application: Application, factory: BaseViewModelFactory) {
        AppInjector.application = application
        localDb = LocalDatabase.getInstance(application)
        AppInjector.application.let {
            preferences =
                SharedPreferencesProvider(it)
            resource =
                ResourceProvider(it)
            BaseAPI.init(application)
            repository = Repository(SampleAPI.getInstance()?.getService()!!, localDb)
            factory.init(application, repository, preferences, resource)
            viewModelFactory = factory
        }
    }

    inline fun <reified T : ViewModel> obtainViewModel(activity: Activity): T =
        ViewModelProvider(activity as AppCompatActivity,
            getViewModelFactory()
        ).get(T::class.java)

    inline fun <reified T : ViewModel> obtainViewModel(fragment: Fragment): T =
        ViewModelProvider(fragment,
            getViewModelFactory()
        ).get(T::class.java)

    inline fun <reified T : ViewModel> obtainViewModel(application: Application): T =
        ViewModelProvider(application as ViewModelStoreOwner,
            getViewModelFactory()
        ).get(T::class.java)

    fun getViewModelFactory():BaseViewModelFactory = viewModelFactory
}