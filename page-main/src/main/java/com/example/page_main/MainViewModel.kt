package com.example.page_main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.GameMenuResponse
import com.example.repository.model.LoginResponse
import com.example.repository.model.ViewState

class MainViewModel(repository: Repository) : ViewModel(){

    private val gameMenuResult: LiveData<ViewState<GameMenuResponse>>
        = repository.getGameMenu("test").asLiveData()
}