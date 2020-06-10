package com.example.page_bet.bet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.repository.Repository
import com.example.repository.constant.BetItemType
import com.example.repository.model.base.ViewState
import com.example.repository.model.base.ViewState.Success
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow

class BetViewModel(var repository: Repository) : ViewModel(){

    /** --------------------------------------- Page's Data --------------------------------------- **/

    var playTypeInfoList: PlayTypeInfoResponse? = null
    var gameId: Int = -1
    var gameTypeId: Int = -1
    var playTypeId: Int = -1
    var betItemType: BetItemType = BetItemType.NONE
    var selectNumber: String = ""
    var issueId: Int = -1

    /** --------------------------------------- Remote --------------------------------------- **/

    //取得當期期號資訊
    fun getCurrentIssueInfo(token:String, gameId: Int): LiveData<ViewState<IssueInfoResponse>>{
        return repository.getIssueInfo(token, gameId).asLiveData()
    }

    //取得最近一期開獎資訊
    fun getLastIssueResult(token:String, gameId: ArrayList<Int>): LiveData<ViewState<LastIssueResultResponse>>{
        return repository.getLastIssueResult(token, gameId).asLiveData()
    }

    //取得該遊戲玩法列表
    fun getPlayTypeInfoList(token:String, gameId: Int): LiveData<ViewState<List<BetTypeEntity>>>{
        return repository.getPlayTypeInfoList(token, gameId).flatMapConcat{ state ->
            flow<ViewState<List<BetTypeEntity>>> {
                when (state) {
                    is Success -> {
                        playTypeInfoList = state.data
                        var betTypeList: ArrayList<BetTypeEntity> = arrayListOf()
                        if(!state.data.data.betTypeGroupList.isNullOrEmpty()){
                            for (item in state.data.data.betTypeGroupList) {
                                betTypeList.addAll(item.betTypeEntityList)
                            }
                        }
                        emit(ViewState.success(betTypeList.toList()))
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

    //取得歷史開獎資訊，預設10期
    fun getLotteryHistoricalRecord(token: String, gameId: Int): LiveData<ViewState<HistoricalResponse>>{
        return repository.getLotteryHistoricalRecord(token, gameId).asLiveData()
    }

    //進行投注
    fun getBetList(token: String, param: BetEntityParam): LiveData<ViewState<BetListResponse>>{
        return repository.getBetList(token,param).asLiveData()
    }

    /** --------------------------------------- Local Database --------------------------------------- **/
    fun addCart(cart: Cart) = repository.addCart(cart)

    fun getCartList(gameId: Int) = repository.getCartList(gameId).asLiveData()

    fun getAllGameId() =  repository.getAllGameId().asLiveData()

}