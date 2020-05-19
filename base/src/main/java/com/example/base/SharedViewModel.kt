package com.example.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.repository.Repository

class SharedViewModel(repository: Repository) :
    ViewModel(){

    var lotteryToken: MutableLiveData<String> = MutableLiveData()
}