package com.example.page_bet.bet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.*
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetItemUtil.getTypeData
import com.example.page_bet.bet.play_type_select.PlayTypeDialog
import com.example.repository.constant.BetItemType
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.coroutines.launch
import me.vponomarenko.injectionmanager.x.XInjectionManager

class BetFragment : BaseFragment() {

    //TODO 這頁面要判斷當下手機是否為16:9的比例來切換顯示Layout

    companion object {
        const val TAG_GAME_ID = "tag_game_id"
        const val TAG_GAME_NAME = "tag_game_name"
        const val TAG_GAME_TYPE = "tag_game_type"
    }

    private lateinit var mViewModel: BetViewModel
    private var mPlayTypeDialog: PlayTypeDialog? = null

    private var mGameID: Int = -1
    private var mGameTypeID: Int = -1
    private var mCurrentBetItemType: BetItemType = BetItemType.NONE

    private var mBetPositionAdapter: BetPositionAdapter? = null
    private var mBetRegionAdapter: BetRegionAdapter? = null

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        Log.e("Ian", "[BetFragment] init")
        mViewModel = AppInjector.obtainViewModel(this)
        arguments?.let {
            ivGameName.text = it.getString(TAG_GAME_NAME, "empty")
            mGameID = it.getInt(TAG_GAME_ID, -1)
            mGameTypeID = it.getInt(TAG_GAME_TYPE, -1)
        }
        refreshCurrentIssueInfo(getSharedViewModel().lotteryToken.value ?: "empty", mGameID)
        refreshLastIssueResult(getSharedViewModel().lotteryToken.value
                                   ?: "empty", mGameID, mGameTypeID)

        //TODO 要先判斷該遊戲有沒有官方跟信用的玩法支援再依此更新官方/信用的顯示資訊，目前該處只有直接去撈官方玩法，所以遇到只有支援信用的遊戲時會顯示空畫面
        initPlayTypeInfo(getSharedViewModel().lotteryToken.value ?: "empty", mGameID)
        ivPlayTypeSelect.onClick { mPlayTypeDialog?.show() }

        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvBetPositionSelect.layoutManager = layoutManager

        mBetPositionAdapter = BetPositionAdapter(listOf())
        mBetPositionAdapter?.setOnItemChildClickListener { adapter, view, position ->
            Log.e("Iam","[BetPositionAdapter] ItemChildClickListener position: $position")
            mBetPositionAdapter?.data?.let {
                for(item in it){
                    item?.getData()?.isSelect = false
                }
            }
            mBetPositionAdapter?.data?.get(position)?.getData()?.isSelect = true
            mBetPositionAdapter?.notifyDataSetChanged()

            //在rvBetPosition點擊後 要改變顯示rvBetRegion的部分
            mBetPositionAdapter?.data?.get(position)?.let {
                var data = listOf(MultipleLotteryEntity(mCurrentBetItemType,it.getData()!!))
                setBetRegionDisplay(data)
            }
        }
        rvBetPositionSelect.adapter = mBetPositionAdapter

        var layout = ScrollableLinearLayoutManager(context,false)
        layout.orientation = LinearLayoutManager.VERTICAL
        rvBetRegion.layoutManager = layout
        mBetRegionAdapter = BetRegionAdapter(listOf())
        rvBetRegion.adapter = mBetRegionAdapter

    }

    //刷新當期投注資訊
    private fun refreshCurrentIssueInfo(token: String, gameId: Int) {
        Log.e("Ian", "[refreshCurrentIssueInfo] token:$token, gameId:$gameId")
        mViewModel.getCurrentIssueInfo(token, gameId).observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    state.data.data?.let {
                        tvCurrentIssueNumber.text = "${it.issueNum.let { issueNum ->
                            if (issueNum.length!! > 7) issueNum.substring(issueNum.length - 7) else issueNum
                        }}期"
                        var leftTime =
                            ((it.buyEndTime.minus(System.currentTimeMillis())).div(1000)).toInt()
                        launch {
                            timer(1000, false) {
                                var time = leftTime--
                                tvCurrentIssueLeftTime.text = getDisplayTime(time)
                                if (time <= 0) {
                                    this.cancel()
                                    refreshCurrentIssueInfo(getSharedViewModel().lotteryToken.value
                                                                ?: "empty", mGameID)
                                    refreshLastIssueResult(getSharedViewModel().lotteryToken.value
                                                               ?: "empty", mGameID, mGameTypeID)
                                }
                            }
                        }
                    }
                }
                is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }

    //刷新最新開獎資訊
    private fun refreshLastIssueResult(token: String, gameId: Int, gameTypeId: Int) {
        Log.e("Ian", "[refreshLastIssueResult] token:$token, gameId:$gameId gameTypeId:$gameTypeId")
        mViewModel.getLastIssueResult(token, gameId).observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    //TODO 根據gametype切換adapter中的item顯示
                    Log.e("Ian", "[refreshLastIssueResult] Success data:${state.data}")

                    tvLastIssueNumber.text =
                        "${state.data.data[0].issueNum.let { if (it.length > 7) it.substring(it.length - 7) else it }}期"

                    var layoutManager = LinearLayoutManager(context)
                    layoutManager.orientation = LinearLayoutManager.VERTICAL
                    rvLastIssueResult.layoutManager = layoutManager

                    var adapter: IssueResultAdapter =
                        IssueResultAdapter(mutableListOf(MultipleIssueResultItem(gameTypeId, state.data.data[0])))
                    rvLastIssueResult.adapter = adapter
                }
                is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }

    private fun initPlayTypeInfo(token: String, gameId: Int) {
        mViewModel.getPlayTypeInfoList(token, gameId).observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    initPlayTypeDialog(state.data)
                }
                is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }
    }

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

    private fun initPlayTypeDialog(data: List<BetTypeEntity>) {
        if (mPlayTypeDialog == null) mPlayTypeDialog = context?.let {
            PlayTypeDialog(it, data, object : PlayTypeDialog.OnPlayTypeSelectListener {
                override fun onSelect(playTypeCode: Int, playTypeName: String, betGroupName: String, betTypeName: String) {
                    Log.e("Ian", "[onPlayTypeSelectListener] playTypeCode:$playTypeCode, playTypeName:$playTypeName, betGroupName:$betGroupName, betTypeName:$betTypeName")
                    tvGamePlayType.text = "$betTypeName $betGroupName $playTypeName"
                    //TODO change投注欄位的ui

                    var result = getTypeData(context!!, playTypeCode.toString())
                    Log.e("Ian", "getTypeData: $result")
                    //根據typeData顯示投注單位選擇，可以根據list的長度來判斷該顯示什麼樣式的投注單位選擇
                    mCurrentBetItemType = result.first
                    var oriList = result.second
                    var modifyList: ArrayList<MultiplePlayTypePositionItem> = arrayListOf()

                    //單式時因為oriList沒有東西所以產不出來，需要在oriList = 0時額外處理
                    if (oriList.isNotEmpty()) {
                        for (item in oriList) {
                            modifyList.add(MultiplePlayTypePositionItem(oriList.size, item))
                        }
                    } else {
                        modifyList.add(MultiplePlayTypePositionItem(0, BetData("單式test", HashMap())))
                    }
//                    Log.e("Ian","itemType:${modifyList?.get(0)?.itemType}, Data:${modifyList?.get(0)?.getData()}")

                    mBetPositionAdapter?.setNewData(modifyList)

                    //TODO 在rvBetPosition點擊後 要改變顯示rvBetRegion的部分
                    mBetRegionAdapter?.setNewData(listOf(MultipleLotteryEntity(result.first, result.second[0])))

                }

            })
        }
    }

    private fun setBetRegionDisplay(data: List<MultipleLotteryEntity>) {
        mBetRegionAdapter?.setNewData(data)
    }
}