package com.example.page_bet.bet_menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.model.MultipleMenuItem
import com.example.repository.model.ViewState
import kotlinx.coroutines.flow.*

class BetMenuViewModel(var repository: Repository) : ViewModel() {

    fun getGameMenuResult(token: String) =
        repository.getGameMenu(token).flatMapConcat { state ->
            flow<ViewState<ArrayList<MultipleMenuItem>>> {
                when (state) {
                    is ViewState.Success -> {
                        var gameMenuList: ArrayList<MultipleMenuItem> = arrayListOf()
                        if(!state.data.data.isNullOrEmpty()){
                            for (item in state.data.data) {
                                var type = when (item.id) {
                                    7 -> MultipleMenuItem.FAVORITE
                                    134 -> MultipleMenuItem.HOT
                                    else -> MultipleMenuItem.NORMAL
                                }
                                gameMenuList.add(
                                    MultipleMenuItem(
                                        type,
                                        data = item
                                    )
                                )
                            }
                        }
                        emit(ViewState.success(gameMenuList))
                    }
                    is ViewState.Loading -> {
                        emit(ViewState.loading())
                    }
                    is ViewState.Error -> {
                        emit(ViewState.error(state.message.orEmpty()))
                    }
                }
            }
        }.asLiveData()
}