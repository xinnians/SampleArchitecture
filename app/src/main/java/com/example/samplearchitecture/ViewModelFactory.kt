package com.example.samplearchitecture

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.base.BaseViewModelFactory
import com.example.base.ResourceProvider
import com.example.base.SharedPreferencesProvider
import com.example.base.SharedViewModel
import com.example.page_bet.bet.BetViewModel
import com.example.page_bet.bet_menu.BetMenuViewModel
import com.example.page_bet.game_favorite.GameFavoriteViewModel
import com.example.page_deposit.DepositViewModel
import com.example.page_bet.lottery_center.LotteryCenterViewModel
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
                isAssignableFrom(MainViewModel::class.java) -> MainViewModel(mRepository!!)
                isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel(repository = mRepository!!)
                isAssignableFrom(BetMenuViewModel::class.java) -> BetMenuViewModel(repository = mRepository!!)
                isAssignableFrom(GameFavoriteViewModel::class.java) -> GameFavoriteViewModel()
                isAssignableFrom(DepositViewModel::class.java) -> DepositViewModel(repository = mRepository!!)
                isAssignableFrom(LotteryCenterViewModel::class.java) -> LotteryCenterViewModel(repository = mRepository!!)
                else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            } as T
        }
    }

}