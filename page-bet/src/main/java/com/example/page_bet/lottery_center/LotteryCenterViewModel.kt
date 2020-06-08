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
import com.example.repository.model.bet.MultipleIssueResultItem
import com.example.repository.model.bet.MultipleMenuItem
import kotlinx.coroutines.flow.map

class LotteryCenterViewModel(private var repository: Repository) : ViewModel() {

    private var mGameLists: ArrayList<GameMenuResponse.Data> = arrayListOf()
    private var mAllGameLastIssueInfo: List<LastIssueResultResponse.Data>? = null

    fun setGameMenuList(list: ArrayList<MultipleMenuItem>) {
        for (index in GameTypeId.values()) {
            var gameList: GameMenuResponse.Data = GameMenuResponse.Data(
                id = index.typeId, gameTypeDisplayName = index.name, gameInfoEntityList = listOf()
            )
            var itemList: MutableList<GameMenuResponse.Data.GameInfoEntity> = mutableListOf()
            list.forEach {
                it.getData()?.gameInfoEntityList?.forEach { gameInfoEntity ->
                    if (gameInfoEntity.gameTypeId == index.typeId && !gameList.gameInfoEntityList.contains(
                            gameInfoEntity
                        )
                    ) {
                        itemList.add(gameInfoEntity)
                    }
                }
            }
            gameList.gameInfoEntityList = itemList
            mGameLists.add(gameList)
        }
    }

    fun getAllGameLastIssueInfo(token: String): LiveData<ViewState<LastIssueResultResponse>> {
        var gameIdList: ArrayList<Int> = arrayListOf()
        mGameLists.forEach { data ->
            data.gameInfoEntityList.forEach {
                gameIdList.add(it.gameId)
            }
        }
        return repository.getLastIssueResult(token, gameIdList).map { state ->
            if (state is ViewState.Success) {
                mAllGameLastIssueInfo = state.data.data
            }
            state
        }.asLiveData()
    }

    fun getIssueInfoList(queryTypeId: GameTypeId): MutableList<MultipleIssueResultItem> {
        var gameIdList: MutableList<Int> = mutableListOf()
        mGameLists.forEach { data ->
            data.gameInfoEntityList.forEach {
                if (it.gameTypeId == queryTypeId.typeId) gameIdList.add(it.gameId)
            }
        }
        Log.e("Ian","[getIssueInfoList] gameIdList:$gameIdList")
        var issueList: MutableList<MultipleIssueResultItem> = mutableListOf()
        mAllGameLastIssueInfo?.let {
            it.forEach { issueData ->
                if (gameIdList.contains(issueData.gameId)) {
                    issueList.add(MultipleIssueResultItem(queryTypeId.typeId, issueData))
                }
            }
        }
        return issueList
    }

    fun getAllIssueInfoList(): MutableList<MutableList<MultipleIssueResultItem>> {
        var resultList: MutableList<MutableList<MultipleIssueResultItem>> = mutableListOf()
        GameTypeId.values().forEach {
            resultList.add(getIssueInfoList(it))
        }
        return resultList
    }

}