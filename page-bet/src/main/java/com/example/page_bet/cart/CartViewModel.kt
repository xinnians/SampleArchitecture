package com.example.page_bet.cart

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.example.base.Event
import com.example.repository.Repository
import com.example.repository.model.base.ViewState
import com.example.repository.room.Cart
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CartViewModel(var repository: Repository, var resources: Resources) : ViewModel() {

    /** --------------------------------------- LiveData --------------------------------------- **/
    var liveLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var liveError: MutableLiveData<String> = MutableLiveData()

    var addCartResult: MutableLiveData<Event<Long>> = MutableLiveData()
    var allGameIdResult: MutableLiveData<MutableList<Int>> = MutableLiveData()
    var delCartResult: MutableLiveData<MutableList<MutableList<Cart>>> = MutableLiveData()
    var updateCartResult: MutableLiveData<Int> = MutableLiveData()
    var getCartListResult: MutableLiveData<MutableList<MutableList<Cart>>> = MutableLiveData()
    var getAllCartListResult: MutableLiveData<Boolean> = MutableLiveData()


    /** --------------------------------------- Local Database --------------------------------------- **/
    //Local Database
    fun addCart(cart: Cart) {
        viewModelScope.launch {
            repository.addCart(cart).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        getAllCartList()
                        addCartResult.value = Event(state.data)
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

    fun getAllGameId() {
        viewModelScope.launch {
            repository.getAllGameId().collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        Log.e("Mori", "ViewState.Success")
                        liveLoading.value = false
                        allGameIdResult.value = state.data
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

    fun delCart(cart: Cart) {
        viewModelScope.launch {
            repository.delCart(cart).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        val result: MutableList<MutableList<Cart>>? = getCartListResult.value
                        result?.apply {
                            map { if (it.contains(cart)) it.remove(cart) }
                        }
                        delCartResult.postValue(result)
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

    fun updateCart(cart: Cart){
        viewModelScope.launch {
            repository.updateCart(cart).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        updateCartResult.postValue(state.data)
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

    fun getCartArray(gameIdArray: ArrayList<Int>) {
        viewModelScope.launch {
            repository.getCartListArray(gameIdArray).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        getCartListResult.value = state.data
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

    fun getAllCartList() {
        viewModelScope.launch {
            repository.getAllCartList().collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        getAllCartListResult.value = state.data.size > 0
                    }
                    is ViewState.Loading -> {
                        liveLoading.value = true
                    }
                    is ViewState.Error -> {
                        liveLoading.value = false
                        liveError.value = state.message
                    }
                }
            }
        }
    }

}