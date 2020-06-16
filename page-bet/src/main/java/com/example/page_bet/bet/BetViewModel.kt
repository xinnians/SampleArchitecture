package com.example.page_bet.bet

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.example.base.timer
import com.example.repository.Repository
import com.example.repository.constant.BetItemType
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class BetViewModel(var repository: Repository, var resources: Resources) : ViewModel() {

    /** --------------------------------------- Page's Data --------------------------------------- **/

    var mGameName: String = ""
    var mGameId: Int = -1
    var mGameTypeId: Int = -1

    var mPlayTypeInfoList: PlayTypeInfoResponse? = null
    var mPlayTypeId: Int = -1
    var mBetItemType: BetItemType = BetItemType.NONE
    var mSelectNumber: String = ""
    var mIssueId: Int = -1
    var mToken: String = ""

    /** --------------------------------------- LiveData --------------------------------------- **/

    var liveCurrentIssueInfo: MutableLiveData<ViewState<IssueInfoResponse>> = MutableLiveData()

    //當期期號
    var liveIssueDisplayNumber: MutableLiveData<String> = MutableLiveData()

    //當期期號剩餘時間
    var liveCurrentIssueLeftTime: MutableLiveData<String> = MutableLiveData()

    //最近一期開獎期號
    var liveLastIssueDisplayNumber: MutableLiveData<String> = MutableLiveData()

    //最近一期開獎號碼
    var liveLastIssueResultItem: MutableLiveData<MutableList<MultipleIssueResultItem>> = MutableLiveData()

    //玩法列表
    var livePlayTypeList: MutableLiveData<ArrayList<BetTypeEntity>> = MutableLiveData()

    //投注欄位選擇列表
    var liveBetPositionList: MutableLiveData<MutableList<MultiplePlayTypePositionItem>> = MutableLiveData()

    //當前玩法名稱顯示
    var liveGamePlayTypeDisPlayName: MutableLiveData<String> = MutableLiveData()

    //顯示預設投注畫面通知
    var liveDefaultUiDisplay: MutableLiveData<Unit> = MutableLiveData()

    //歷史開獎紀錄10筆
    var liveHistoryRecordList: MutableLiveData<MutableList<MultipleHistoryRecord>> = MutableLiveData()

    //投注欄位列表
    var liveBetRegionList: MutableLiveData<MutableList<MultipleLotteryEntity>> = MutableLiveData()

    //當前選擇總注數
    var liveBetCount: MutableLiveData<Int> = MutableLiveData()

    //當前選擇總金額
    var liveBetCurrency: MutableLiveData<Int> = MutableLiveData()

    var liveLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    var liveError: MutableLiveData<String> = MutableLiveData()

    /** --------------------------------------- Remote --------------------------------------- **/

    //取得當期期號資訊
    fun getCurrentIssueInfo(token: String, gameId: Int) {
        viewModelScope.launch {
            repository.getIssueInfo(token, gameId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        state.data.data?.let { data ->
                            liveIssueDisplayNumber.value = "${data.issueNum.let { issueNum ->
                                if (issueNum.length > 7) issueNum.substring(issueNum.length - 7) else issueNum
                            }}期"
                            mIssueId = data.issueId
                            var leftTime = ((data.buyEndTime.minus(System.currentTimeMillis())).div(1000)).toInt()
                            timer(1000, false) {
                                var time = leftTime--
                                liveCurrentIssueLeftTime.value = getDisplayTime(time)
                                if (time <= 0) {
                                    this.cancel()
                                    //更新當期資訊
                                    getCurrentIssueInfo(token, gameId)
                                    //TODO 更新最近一期開獎資訊
                                }
                            }
                        }
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

    //取得最近一期開獎資訊
    fun getLastIssueResult(token: String, gameId: ArrayList<Int>, gameTypeId: Int) {
        viewModelScope.launch {
            repository.getLastIssueResult(token, gameId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        liveLastIssueDisplayNumber.value =
                            "${state.data.data[0].issueNum.let { if (it.length > 7) it.substring(it.length - 7) else it }}期"
                        liveLastIssueResultItem.value = mutableListOf(MultipleIssueResultItem(gameTypeId, state.data.data[0]))
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

    //取得該遊戲玩法列表
    fun getPlayTypeInfoList(token: String, gameId: Int) {
        viewModelScope.launch {
            repository.getPlayTypeInfoList(token, gameId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        mPlayTypeInfoList = state.data
                        var betTypeList: ArrayList<BetTypeEntity> = arrayListOf()
                        if (!state.data.data.betTypeGroupList.isNullOrEmpty()) {
                            for (item in state.data.data.betTypeGroupList) {
                                betTypeList.addAll(item.betTypeEntityList)
                            }
                        }
                        livePlayTypeList.value = betTypeList
                        initDeafaultDisplay()
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

    private fun initDeafaultDisplay() {
        Log.e("Ian", "[initDefaultSelect] call.")
        var defaultPlayTypeCode: Int = -1
        var defaultPlayTypeName: String = ""
        var betTypeDisplayName: String = ""
        var betGroupDisplayName: String = ""
        var playTypeDisplayName: String = ""

        try {
            mPlayTypeInfoList?.data?.betTypeGroupList?.get(0)?.let { betTypeGroup ->
                betTypeGroup.betTypeEntityList?.get(0)?.let { betTypeEntity ->
                    betTypeDisplayName = betTypeEntity.betTypeDisplayName
                    betTypeEntity.mobileBetGroupEntityList?.get(0)?.let { betGroupEntity ->
                        betGroupDisplayName = betGroupEntity.betGroupDisplayName
                        betGroupEntity.playTypeInfoEntityList?.get(0)?.let { playTypeInfoEntity ->
                            playTypeDisplayName = playTypeInfoEntity.displayName
                            defaultPlayTypeName = "$betTypeDisplayName $betGroupDisplayName $playTypeDisplayName"
                            defaultPlayTypeCode = playTypeInfoEntity.playTypeCode
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("[initDefaultSelect]", "exception: ${e.message}")
        }

        liveGamePlayTypeDisPlayName.value = defaultPlayTypeName

        mPlayTypeId = defaultPlayTypeCode
        var result = BetItemUtil.getTypeData(resources, mPlayTypeId.toString())
        mBetItemType = result.first
        var oriList = result.second
        var modifyList: MutableList<MultiplePlayTypePositionItem> = arrayListOf()

        //單式時因為oriList沒有東西所以產不出來，需要在oriList = 0時額外處理
        if (oriList.isNotEmpty()) {
            for (item in oriList) {
                modifyList.add(MultiplePlayTypePositionItem(if (mBetItemType == BetItemType.SINGLE_BET_TYPE) 0 else oriList.size, item))
            }
        } else {
            modifyList.add(MultiplePlayTypePositionItem(0, BetData("沒有管理到", arrayListOf())))
        }

        liveBetPositionList.value = modifyList
        liveDefaultUiDisplay.value = Unit
    }

    //取得歷史開獎資訊，預設10期
    fun getLotteryHistoricalRecord(token: String, gameId: Int) {
        viewModelScope.launch {
            repository.getLotteryHistoricalRecord(token, gameId).collect { state ->
                when (state) {
                    is ViewState.Success -> {
                        liveLoading.value = false
                        var list: MutableList<MultipleHistoryRecord> = mutableListOf()
                        state.data.data.forEach {
                            list.add(MultipleHistoryRecord(mGameTypeId, it))
                        }
                        liveHistoryRecordList.value = list
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

    //進行投注
    fun getBetList(token: String, param: BetEntityParam): LiveData<ViewState<BetListResponse>> {
        return repository.getBetList(token, param).asLiveData()
    }

    /** --------------------------------------- Local Database --------------------------------------- **/
    //Local Database
    fun addCart(cart: Cart) = repository.addCart(cart).asLiveData()

    fun getCartList(gameId: Int) = repository.getCartList(gameId).asLiveData()

    fun getAllGameId() = repository.getAllGameId().asLiveData()

    fun delCart(cart: Cart) = repository.delCart(cart).asLiveData()

    fun updateCart(cart: Cart) = repository.updateCart(cart).asLiveData()

    fun getCartArray(gameIdArray: ArrayList<Int>) = repository.getCartListArray(gameIdArray).asLiveData()

    /** --------------------------------------- Utils --------------------------------------- **/
    private fun getDisplayTime(second: Int): String {
        var h = 0
        var d = 0
        var s = 0
        val temp = second % 3600
        if (second > 3600) {
            h = second / 3600
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60
                    if (temp % 60 != 0) {
                        s = temp % 60
                    }
                } else {
                    s = temp
                }
            }
        } else {
            d = second / 60
            if (second % 60 !== 0) {
                s = second % 60
            }
        }
        return "$h : $d : $s"
    }

    /** --------------------------------------- action --------------------------------------- **/

    //選擇玩法
    var selectPlayType = { playTypeCode: Int, playTypeName: String, betGroupName: String, betTypeName: String ->
        liveGamePlayTypeDisPlayName.value = "$betTypeName $betGroupName $playTypeName"
        //儲存目前的playTypeCode，以便計算注數等判斷
        mPlayTypeId = playTypeCode
        var result = BetItemUtil.getTypeData(resources, playTypeCode.toString())
        //根據typeData顯示投注單位選擇，可以根據list的長度來判斷該顯示什麼樣式的投注單位選擇
        mBetItemType = result.first
        var oriList = result.second
        var modifyList: ArrayList<MultiplePlayTypePositionItem> = arrayListOf()
        //單式時因為oriList沒有東西所以產不出來，需要在oriList = 0時額外處理
        if (oriList.isNotEmpty()) {
            var type = if (mBetItemType == BetItemType.SINGLE_BET_TYPE) 0 else oriList.size
            for (item in oriList) {
                modifyList.add(MultiplePlayTypePositionItem(type, item))
            }
        } else {
            modifyList.add(MultiplePlayTypePositionItem(0, BetData("單式test", arrayListOf())))
        }
        liveBetPositionList.value = modifyList
        liveDefaultUiDisplay.value = Unit
    }

    //選擇投注欄位
    var selectBetPosition = { position: Int, data: MutableList<MultiplePlayTypePositionItem> ->
        data.apply {
            map { it.getData()?.isSelect = false }
            //在rvBetPosition點擊後 要改變顯示rvBetRegion的部分
            get(position).getData()?.apply {
                isSelect = true
                val data = mutableListOf(MultipleLotteryEntity(mBetItemType.unitDisplayMode, this))
                liveBetRegionList.value = data
            }
            liveBetPositionList.value = this
        }
    }

    //選擇該欄位投注數值
    var selectBetRegion = {
        var selectNumber = liveBetPositionList.value?.toList()?.let { BetCountUtil.getBetSelectNumber(mPlayTypeId, it) }
        mSelectNumber = selectNumber?.betNumber.toString()

        livePlayTypeList.value?.let { betTypeList ->
            for (betType in betTypeList) {
                for (betGroup in betType.mobileBetGroupEntityList) {
                    for (playType in betGroup.playTypeInfoEntityList) {
                        if (playType.playTypeCode == mPlayTypeId) {
                            Log.e("Ian", "playType.regex: ${playType.regex}")
                            //計算注數時，將該玩法的正則丟入判斷該投注號碼是否符合規則。
                            var count = selectNumber?.let { BetCountUtil.getBetCount(it, playType.regex) }
                            Log.e("Ian", "count:$count")
                            liveBetCount.value = count
                            liveBetCurrency.value = count?.times(Math.random()*1000)?.toInt()
                        }
                    }
                }
            }
        }
    }
}