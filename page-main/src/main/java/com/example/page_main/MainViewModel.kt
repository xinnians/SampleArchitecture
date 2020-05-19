package com.example.page_main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository

class MainViewModel(private val repository: Repository) : ViewModel() {

    fun getGameMenuResult(token: String) = repository.getGameMenu(token).asLiveData()
}