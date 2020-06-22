package com.example.page_bet.bet

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.*
import com.example.base.timer
import com.example.base.toSingleEvent
import com.example.repository.Repository
import com.example.repository.constant.BetItemType
import com.example.repository.constant.playTypeID_206010
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class BetViewModel(var repository: Repository, var resources: Resources) : ViewModel() {

    /** --------------------------------------- Page's Data --------------------------------------- **/

    var mGameName: String = ""
    var mGameId: Int = -1
    var mGameTypeId: Int = -1

    var mPlayTypeInfoList: PlayTypeInfoResponse? = null
    var mPlayTypeId: Int = -1
    var mBetItemType: BetItemType = BetItemType.NONE
    var mSecondBetItemType: BetItemType = BetItemType.NONE
    var mSelectNumber: String = ""
    var mIssueId: Int = -1
    var mToken: String = ""

    var mUnitValue: Double = 1.0
    var mCurrencyUnit: Int = 1
    var mMultiple: Int = 1

    var mCurrentIssueTimerJob: Job? = null

    /** --------------------------------------- LiveData --------------------------------------- **/

    var liveCurrentIssueInfo: MutableLiveData<ViewState<IssueInfoResponse>> = MutableLiveData()

    //當期期號
    var liveIssueDisplayNumber: MutableLiveData<String> = MutableLiveData<String>().toSingleEvent()

    //當期期號剩餘時間
    var liveCurrentIssueLeftTime: MutableLiveData<String> = MutableLiveData<String>().toSingleEvent()

    //最近一期開獎期號
    var liveLastIssueDisplayNumber: MutableLiveData<String> = MutableLiveData<String>().toSingleEvent()

    //最近一期開獎號碼
    var liveLastIssueResultItem: MutableLiveData<MutableList<MultipleIssueResultItem>> = MutableLiveData<MutableList<MultipleIssueResultItem>>().toSingleEvent()

    //玩法列表
    var livePlayTypeList: MutableLiveData<ArrayList<BetTypeEntity>> = MutableLiveData<ArrayList<BetTypeEntity>>().toSingleEvent()

    //投注欄位選擇列表
    var liveBetPositionList: MutableLiveData<MutableList<MultiplePlayTypePositionItem>> = MutableLiveData<MutableList<MultiplePlayTypePositionItem>>().toSingleEvent()

    //當前玩法名稱顯示
    var liveGamePlayTypeDisPlayName: MutableLiveData<String> = MutableLiveData<String>().toSingleEvent()

    //顯示預設投注畫面通知
    var liveDefaultUiDisplay: MutableLiveData<Unit> = MutableLiveData<Unit>().toSingleEvent()

    //歷史開獎紀錄10筆
    var liveHistoryRecordList: MutableLiveData<MutableList<MultipleHistoryRecord>> = MutableLiveData<MutableList<MultipleHistoryRecord>>().toSingleEvent()

    //投注欄位列表
    var liveBetRegionList: MutableLiveData<MutableList<MultipleLotteryEntity>> = MutableLiveData<MutableList<MultipleLotteryEntity>>().toSingleEvent()

    //當前選擇總注數
    var liveBetCount: MutableLiveData<Int> = MutableLiveData<Int>().toSingleEvent()

    //當前選擇總金額
    var liveBetCurrency: MutableLiveData<Int> = MutableLiveData<Int>().toSingleEvent()

    //是否需顯示全螢幕切換按鈕
    var liveIsNeedShowFullScreen: MutableLiveData<Boolean> = MutableLiveData<Boolean>().toSingleEvent()

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

                            mCurrentIssueTimerJob?.cancel()

                            mCurrentIssueTimerJob = timer(1000, false) {
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
        mSecondBetItemType = result.third
        var modifyList: MutableList<MultiplePlayTypePositionItem> = arrayListOf()

        //單式時因為oriList沒有東西所以產不出來，需要在oriList = 0時額外處理
        if (oriList.isNotEmpty()) {
            for (item in oriList) {
                modifyList.add(MultiplePlayTypePositionItem(if (mBetItemType == BetItemType.SINGLE_BET_TYPE) 0 else oriList.size, item))
            }
        } else {
            modifyList.add(MultiplePlayTypePositionItem(0, BetData(displayTitle = "沒有管理到",unitList =  arrayListOf(),betItemType = mBetItemType)))
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
        mSecondBetItemType = result.third
        var modifyList: ArrayList<MultiplePlayTypePositionItem> = arrayListOf()
        //單式時因為oriList沒有東西所以產不出來，需要在oriList = 0時額外處理
        if (oriList.isNotEmpty()) {
            var type = if (mBetItemType == BetItemType.SINGLE_BET_TYPE || mBetItemType == BetItemType.ANY_SINGLE_BET_TYPE) 0 else oriList.size
            for (item in oriList) {
                modifyList.add(MultiplePlayTypePositionItem(type, item))
            }
        } else {
            modifyList.add(MultiplePlayTypePositionItem(0, BetData("單式test", arrayListOf(),betItemType = mBetItemType)))
        }
        liveIsNeedShowFullScreen.value = (mBetItemType == BetItemType.ANY_SINGLE_BET_TYPE || mBetItemType == BetItemType.SINGLE_BET_TYPE)

        liveBetPositionList.value = modifyList
        liveDefaultUiDisplay.value = Unit

        //選擇玩法後初始化注數和總金額UI
        liveBetCount.value = 0
        liveBetCurrency.value = getTotalCurrency().toInt()
    }

    //選擇投注欄位
    var selectBetPosition = { position: Int, data: MutableList<MultiplePlayTypePositionItem> ->
        data.apply {
            map { it.getData()?.isSelect = false }
            //在rvBetPosition點擊後 要改變顯示rvBetRegion的部分
            get(position).getData()?.apply {
                Log.e("Ian","[selectBetPosition] BetData:$this")
                isSelect = true
                val data = if(mPlayTypeId.toString() == playTypeID_206010 && position == 1){
                    mutableListOf(MultipleLotteryEntity(mSecondBetItemType.unitDisplayMode, this))
                }else
                    mutableListOf(MultipleLotteryEntity(mBetItemType.unitDisplayMode, this))

                liveBetRegionList.value = data
            }
            liveBetPositionList.value = this
        }
    }

    //選擇該欄位投注數值
    var selectBetRegion = {
        var selectNumber = liveBetPositionList.value?.toList()?.let {
            it.forEach { item ->
                Log.e("Ian","[selectBetRegion] ${item.getData()}")
            }
            BetCountUtil.getBetSelectNumber(mPlayTypeId, it) }
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
                            liveBetCurrency.value = getTotalCurrency().toInt()
                        }
                    }
                }
            }
        }
    }

    var selectBetUnit = { unit: Double, currency: Int ->
        mCurrencyUnit = currency
        mUnitValue = unit
        liveBetCurrency.value = getTotalCurrency().toInt()
    }

    var selectBetMultiple = { value: Int ->
        mMultiple = value
        liveBetCurrency.value = getTotalCurrency().toInt()
    }

    private fun getTotalCurrency(): Double {
        var result = liveBetCount.value?.times(mCurrencyUnit * mUnitValue * mMultiple) ?: 0.0
        Log.e("Ian", "[getTotalCurrency] result:$result")
        return result
    }

}