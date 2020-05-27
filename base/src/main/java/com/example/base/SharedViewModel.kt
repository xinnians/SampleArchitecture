package com.example.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.repository.Repository
import com.example.repository.model.bet.MultipleMenuItem

class SharedViewModel(repository: Repository) :
    ViewModel(){

    var lotteryToken: MutableLiveData<String> = MutableLiveData()
    var gameMenuList: MutableLiveData<ArrayList<MultipleMenuItem>> = MutableLiveData()
}