package com.example.page_bet.bet_menu

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.GameMenuResponse
import com.example.repository.model.ViewState
import kotlinx.coroutines.flow.*

class BetMenuViewModel(var repository: Repository) : ViewModel() {

    fun getGameMenuResult(token: String) =
//        repository.getGameMenu(token).map {state ->
//            when(state){
//                is ViewState.Success -> {
//                    var gameMenuList: ArrayList<MultipleMenuItem> = arrayListOf()
//                    for(item in state.data.data){
//                        var type = when(item.id){
//                            7 -> MultipleMenuItem.FAVORITE
//                            134 -> MultipleMenuItem.HOT
//                            else -> MultipleMenuItem.NORMAL
//                        }
//                        gameMenuList.add(MultipleMenuItem(type,data = item))
//                    }
//                }
//            }
//        }.asLiveData()
        repository.getGameMenu(token)
            .map { state ->
                when (state) {
                    is ViewState.Success -> {
                        var gameMenuList: ArrayList<MultipleMenuItem> = arrayListOf()
                        for (item in state.data.data) {
                            var type = when (item.id) {
                                7 -> MultipleMenuItem.FAVORITE
                                134 -> MultipleMenuItem.HOT
                                else -> MultipleMenuItem.NORMAL
                            }
                            gameMenuList.add(MultipleMenuItem(type, data = item))
                        }
                    }
                }
            }.transform<ViewState<GameMenuResponse>, ViewState<ArrayList<MultipleMenuItem>>> {  }
}