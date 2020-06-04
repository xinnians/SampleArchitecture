package com.example.page_bet.lottery_center

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.constant.GameTypeId
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.GameMenuResponse
import com.example.repository.model.bet.LastIssueResultResponse
import com.example.repository.model.bet.MultipleMenuItem

class LotteryCenterViewModel(private var repository: Repository) : ViewModel() {

    private var mGameLists: ArrayList<GameMenuResponse.Data> = arrayListOf()

    fun setGameMenuList(list: ArrayList<MultipleMenuItem>) {
        for (index in GameTypeId.values()) {
            var gameList: GameMenuResponse.Data =
                GameMenuResponse.Data(id = index.typeId, gameTypeDisplayName = index.name, gameInfoEntityList = listOf())
            var itemList: MutableList<GameMenuResponse.Data.GameInfoEntity> = mutableListOf()
            list.forEach {
                it.getData()?.gameInfoEntityList?.forEach { gameInfoEntity ->
                    Log.e("Ian","[setGameMenuList] gameInfoEntity:$gameInfoEntity")
                    if (gameInfoEntity.gameTypeId == index.typeId && !gameList.gameInfoEntityList.contains(gameInfoEntity)) {
                        itemList.add(gameInfoEntity)
                    }
                }
            }
            gameList.gameInfoEntityList = itemList
            mGameLists.add(gameList)
        }

        Log.e("Ian","[setGameMenuList] mGameLists:$mGameLists")
    }

    fun getAllGameLastIssueInfo(token: String): LiveData<ViewState<LastIssueResultResponse>>{
        var gameIdList: ArrayList<Int> = arrayListOf()
        mGameLists.forEach {data ->
            data.gameInfoEntityList.forEach {
                gameIdList.add(it.gameId)
            }
        }
        return repository.getLastIssueResult(token,gameIdList).asLiveData()
    }

    fun getGameIdList(type: GameTypeId? = null) {
        if(type == null){
            return
        }
    }

}