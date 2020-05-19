package com.example.page_login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.LoginResponse
import com.example.repository.model.ViewState

class LoginViewModel(repository: Repository) : ViewModel() {
    private val loginResult: LiveData<ViewState<LoginResponse.Data>> =
        repository.getLoginResult().asLiveData()

    fun getLoginResult(): LiveData<ViewState<LoginResponse.Data>> = loginResult
}