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

object AppInjector {

    lateinit var application: Application private set
    lateinit var viewModelFactory: BaseViewModelFactory private set
    lateinit var repository: Repository private set
    lateinit var preferences: SharedPreferencesProvider private set
    lateinit var resource: ResourceProvider private set

    fun init(application: Application, factory: BaseViewModelFactory) {
        AppInjector.application = application

        AppInjector.application.let {
            preferences =
                SharedPreferencesProvider(it)
            resource =
                ResourceProvider(it)
            BaseAPI.init(application)
            repository = Repository(SampleAPI.getInstance()?.getService()!!)
            factory.init(application, repository, preferences, resource)
            viewModelFactory = factory
        }
    }

    inline fun <reified T : ViewModel> obtainViewModel(activity: Activity): T =
        ViewModelProvider(activity as AppCompatActivity,
            viewModelFactory
        ).get(T::class.java)

    inline fun <reified T : ViewModel> obtainViewModel(fragment: Fragment): T =
        ViewModelProvider(fragment,
            viewModelFactory
        ).get(T::class.java)

    inline fun <reified T : ViewModel> obtainViewModel(application: Application): T =
        ViewModelProvider(application as ViewModelStoreOwner,
            viewModelFactory
        ).get(T::class.java)
}