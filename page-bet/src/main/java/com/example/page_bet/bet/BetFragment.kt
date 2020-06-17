package com.example.page_bet.bet

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.*
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.base.widget.BetMultipleSelector
import com.example.base.widget.BetUnitSelector
import com.example.base.widget.CustomSwitch
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.lottery_record.LotteryRecordDialog
import com.example.page_bet.bet.play_type_select.PlayTypeDialog
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import kotlinx.android.synthetic.main.fragment_bet.*
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
    private var mLotteryHistoryDialog: LotteryRecordDialog? = null

    private var mBetPositionAdapter: BetPositionAdapter? = null
    private var mBetRegionAdapter: BetRegionAdapter? = null
    private var mIssueResultAdapter: IssueResultAdapter? = null

    private var isBetUnitShow = true
    private var isZoomIn = false

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
        initListener()
        initBinding()
        initUI()
    }

    private fun initData() {
        mViewModel = AppInjector.obtainViewModel(this)
        arguments?.let {
            mViewModel.mGameName = it.getString(TAG_GAME_NAME, "empty")
            mViewModel.mGameId = it.getInt(TAG_GAME_ID, -1)
            mViewModel.mGameTypeId = it.getInt(TAG_GAME_TYPE, -1)
        }
    }

    private fun initView() {
        var issueResultLayoutManager = LinearLayoutManager(context)
        issueResultLayoutManager.orientation = LinearLayoutManager.VERTICAL
        rvLastIssueResult.layoutManager = issueResultLayoutManager
        mIssueResultAdapter = IssueResultAdapter(mutableListOf())
        rvLastIssueResult.adapter = mIssueResultAdapter

        var betPositionSelectLayoutManager = LinearLayoutManager(context)
        betPositionSelectLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        rvBetPositionSelect.layoutManager = betPositionSelectLayoutManager
        mBetPositionAdapter = BetPositionAdapter(listOf())
        rvBetPositionSelect.adapter = mBetPositionAdapter
        mBetPositionAdapter?.bindToRecyclerView(rvBetPositionSelect)

        var layout = ScrollableLinearLayoutManager(context, false)
        layout.orientation = LinearLayoutManager.VERTICAL
        rvBetRegion.layoutManager = layout
        mBetRegionAdapter = BetRegionAdapter(listOf())
        rvBetRegion.adapter = mBetRegionAdapter

        csPlayType.switchType = CustomSwitch.GAME_TYPE
        csPlayRate.switchType = CustomSwitch.GAME_RATE
    }

    private fun initListener() {
        viewBetUnitSelector.addOnUnitSelectListener(object : BetUnitSelector.OnUnitSelectListener {
            override fun onSelect(unitValue: Double, currency: Int) {
                mViewModel.selectBetUnit(unitValue, currency)
            }
        })

        viewBetMultipleSelector.setOnMultipleValueChangeListener(object : BetMultipleSelector.OnMultipleValueChangeListener{
            override fun onChange(value: Int) {
                mViewModel.selectBetMultiple(value)
            }
        })

        ivPlayTypeSelect.onClick { mPlayTypeDialog?.show() }

        mBetPositionAdapter?.setOnItemChildClickListener { _, _, position ->
            Log.e("Iam", "[BetPositionAdapter] ItemChildClickListener position: $position")
            mBetPositionAdapter?.data?.let { mViewModel.selectBetPosition(position, it) }
        }

        mBetRegionAdapter?.setOnUnitClickListener(object : BetRegionAdapter.OnUnitClickListener {
            override fun onUnitClick() {
                mViewModel.selectBetRegion.invoke()
            }
        })

        btnBet.onClick {
            //TODO 根據獎金盤/信用盤的不同 call的api跟參數皆有差異
            //TODO 先判斷當前選擇是否符合可以下注的選擇，可以的話再進行下注的動作。
            var para: BetEntityParam = BetEntityParam(mViewModel.mIssueId,
                                                      arrayListOf(BonusOrderEntity(betCurrency = 1,
                                                                                   betUnit = 1.0,
                                                                                   multiple = 1,
                                                                                   rebate = 0.0,
                                                                                   uuid = "uuid",
                                                                                   amount = 1,
                                                                                   playTypeCode = mViewModel.mPlayTypeId,
                                                                                   betNumber = mViewModel.mSelectNumber,
                                                                                   betCount = 10000)))
            Log.e("Ian", "[getBetList] param: $para")
            mViewModel.getBetList(getSharedViewModel().lotteryToken.value ?: "empty", para).observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        Log.e("Ian", "[getBetList] ViewState.success: data:${state.data}")
                    }
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }
            }
        }

        val zoomIn = ConstraintSet()
        val zoomInTopRow = ConstraintSet()
        val zoomOut = ConstraintSet()
        val zoomOutTopRow = ConstraintSet()
        val mainInLayout = ConstraintSet()
        val mainOutLayout = ConstraintSet()
        mainInLayout.clone(layoutBetMain)
        mainOutLayout.clone(layoutBetMain)
        zoomIn.clone(clTopLayout)
        zoomOut.clone(clTopLayout)
        zoomInTopRow.clone(layoutCurrentIssueInfo)
        zoomOutTopRow.clone(layoutCurrentIssueInfo)

        btnZoom.onClick {
            if (isZoomIn) {
                //恢復原來大小
                val zoomOutTransition = AutoTransition()
                zoomOutTransition.addListener(object : Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {
                        var layout = ScrollableLinearLayoutManager(context, false)
                        layout.orientation = LinearLayoutManager.VERTICAL
                        rvBetRegion.layoutManager = layout

                        mBetPositionAdapter?.setFirstItemSelect()
                    }

                    override fun onTransitionResume(transition: Transition?) {
                    }

                    override fun onTransitionPause(transition: Transition?) {
                    }

                    override fun onTransitionCancel(transition: Transition?) {
                    }

                    override fun onTransitionStart(transition: Transition?) {
                    }
                })
                TransitionManager.beginDelayedTransition(layoutBetMain, zoomOutTransition)
                zoomOut.applyTo(clTopLayout)
                zoomOutTopRow.applyTo(layoutCurrentIssueInfo)
                mainOutLayout.applyTo(layoutBetMain)
                btnZoom.background = drawable(R.drawable.bg_zoom_in)
                isZoomIn = false
            } else {
                //放大投注區
                val zoomInTransition = AutoTransition()
                zoomInTransition.addListener(object : Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {

                        var layout = ScrollableLinearLayoutManager(context, true)
                        layout.orientation = LinearLayoutManager.VERTICAL
                        rvBetRegion.layoutManager = layout

                        var list: ArrayList<MultipleLotteryEntity> = arrayListOf()
                        mBetPositionAdapter?.data?.forEach {
                            list.add(MultipleLotteryEntity(mViewModel.mBetItemType.unitDisplayMode, it.getData()!!, true))
                        }
                        setBetRegionDisplay(list)

                    }

                    override fun onTransitionResume(transition: Transition?) {
                    }

                    override fun onTransitionPause(transition: Transition?) {
                    }

                    override fun onTransitionCancel(transition: Transition?) {
                    }

                    override fun onTransitionStart(transition: Transition?) {
                    }
                })
                TransitionManager.beginDelayedTransition(layoutBetMain, zoomInTransition)
                zoomInView(zoomIn, R.id.toolbar, 1).applyTo(clTopLayout)
                zoomInView(zoomIn, R.id.clTopRow2, 1).applyTo(clTopLayout)
                zoomInView(zoomIn, R.id.clTopRow3, 1).applyTo(clTopLayout)
                zoomInView(zoomInTopRow, R.id.csPlayType, 1).applyTo(layoutCurrentIssueInfo)

                zoomInView(zoomInTopRow, R.id.tvCurrentIssueLeftTime, 2).applyTo(layoutCurrentIssueInfo)
                zoomInView(zoomInTopRow, R.id.tvCurrentIssueNumber, 3).applyTo(layoutCurrentIssueInfo)

                zoomInView(mainInLayout, R.id.btnZoom, 4).applyTo(layoutBetMain)
                btnZoom.background = drawable(R.drawable.bg_zoom_out)
                isZoomIn = true
            }
        }

        val showRate = ConstraintSet()
        val hideRate = ConstraintSet()
        showRate.clone(clUnderLayout)
        hideRate.clone(clUnderLayout)
        tvLabel.onClick {
            if (isBetUnitShow) {
                TransitionManager.beginDelayedTransition(clUnderLayout)
                hideRate.apply {
                    clear(R.id.clRateLayout)
                    connect(R.id.clRateLayout, ConstraintSet.TOP, R.id.clBottomLayout, ConstraintSet.TOP)
                    connect(R.id.clRateLayout, ConstraintSet.START, R.id.clBottomLayout, ConstraintSet.START)
                    connect(R.id.clRateLayout, ConstraintSet.END, R.id.clBottomLayout, ConstraintSet.END)
                }.applyTo(clUnderLayout)
                tvLabel.text = "Show"
                isBetUnitShow = false
            } else {
                TransitionManager.beginDelayedTransition(clUnderLayout)
                showRate.applyTo(clUnderLayout)
                tvLabel.text = "Hide"
                isBetUnitShow = true
            }
        }

        csPlayType.setOnSwitchCallListener(object : CustomSwitch.OnSwitchCall {
            override fun onCall(type: Boolean) {

            }
        })

        csPlayRate.setOnSwitchCallListener(object : CustomSwitch.OnSwitchCall {
            override fun onCall(type: Boolean) {

            }
        })

        ivMoreIssueHistory.onClick {
            mViewModel.getLotteryHistoricalRecord(getSharedViewModel().lotteryToken.value.toString(), mViewModel.mGameId)
        }

        ivAddToShoppingCart.onClick {
            var cart = Cart(0,
                            mViewModel.mIssueId,
                            gameId = mViewModel.mGameId,
                            playTypeCode = mViewModel.mPlayTypeId.toInt(),
                            betNumber = mViewModel.mSelectNumber,
                            betCurrency = 1,
                            betUnit = 1.0,
                            multiple = 1,
                            rebate = 0.0,
                            uuid = "uuid",
                            betCount = 10000,
                            amount = 1)

            mViewModel.addCart(cart).observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        Log.e("Mori", "ViewState.Success")
                        if (-1L != state.data) {
                            toast("加入購物車成功")
                        }
                    }
                    is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
                }
            }
        }

        ivShoppingCart.onClick {
            navigation.toShoppingCartPage()
        }
    }

    private fun initBinding() {
        mViewModel.liveIssueDisplayNumber.observeNotNull(this) {
            tvCurrentIssueNumber.text = it
        }
        mViewModel.liveCurrentIssueLeftTime.observeNotNull(this) {
            tvCurrentIssueLeftTime.text = it
        }
        mViewModel.liveLastIssueDisplayNumber.observeNotNull(this) {
            tvLastIssueNumber.text = it
        }
        mViewModel.liveLastIssueResultItem.observeNotNull(this) {
            mIssueResultAdapter?.setNewData(it)
        }
        mViewModel.livePlayTypeList.observeNotNull(this) {
            initPlayTypeDialog(it)
        }
        mViewModel.liveBetPositionList.observeNotNull(this) {
            mBetPositionAdapter?.setNewData(it)
        }
        mViewModel.liveDefaultUiDisplay.observeNotNull(this) {
            mBetPositionAdapter?.setFirstItemSelect()
        }
        mViewModel.liveHistoryRecordList.observeNotNull(this) {
            showHistoryDialog(it)
        }
        mViewModel.liveGamePlayTypeDisPlayName.observeNotNull(this) {
            tvGamePlayType.text = it
        }
        mViewModel.liveBetRegionList.observeNotNull(this) {
            mBetRegionAdapter?.setNewData(it)
        }
        mViewModel.liveBetCount.observeNotNull(this) {
            tvCount.text = it.toString()
        }
        mViewModel.liveBetCurrency.observeNotNull(this) {
            tvCurrency.text = it.toString()
        }
    }

    private fun initUI() {
        mViewModel.getCurrentIssueInfo(getSharedViewModel().lotteryToken.value.toString(), mViewModel.mGameId)
        mViewModel.getLastIssueResult(getSharedViewModel().lotteryToken.value.toString(), arrayListOf(mViewModel.mGameId), mViewModel.mGameTypeId)
        //TODO 要先判斷該遊戲有沒有官方跟信用的玩法支援再依此更新官方/信用的顯示資訊，目前該處只有直接去撈官方玩法，所以遇到只有支援信用的遊戲時會顯示空畫面
        mViewModel.getPlayTypeInfoList(getSharedViewModel().lotteryToken.value.toString(), mViewModel.mGameId)
    }

    private fun initPlayTypeDialog(data: List<BetTypeEntity>) {
        if (mPlayTypeDialog == null) mPlayTypeDialog = context?.let {
            //讓玩法選擇預設顯示第一個類別，否則一開始點進去會白白的，比較不輕易近人
            if (data.isNotEmpty()) data[0].isSelect = true
            PlayTypeDialog(it, data, object : PlayTypeDialog.OnPlayTypeSelectListener {
                override fun onSelect(playTypeCode: Int, playTypeName: String, betGroupName: String, betTypeName: String) {
                    mViewModel.selectPlayType.invoke(playTypeCode, playTypeName, betGroupName, betTypeName)
                }
            })
        }
    }

    private fun showHistoryDialog(list: MutableList<MultipleHistoryRecord>) {
        mLotteryHistoryDialog = context?.let { LotteryRecordDialog(list, it) }
        mLotteryHistoryDialog?.show()
    }

    private fun setBetRegionDisplay(data: List<MultipleLotteryEntity>) {
        mBetRegionAdapter?.setNewData(data)
    }

    private fun zoomInView(set: ConstraintSet, resId: Int, type: Int): ConstraintSet {
        return set.apply {
            clear(resId, ConstraintSet.TOP)
            clear(resId, ConstraintSet.START)
            clear(resId, ConstraintSet.BOTTOM)
            clear(resId, ConstraintSet.END)
            when (type) {
                1 -> {
                    constrainWidth(resId, 0)
                    constrainHeight(resId, 0)
                    setVisibility(resId, ConstraintSet.INVISIBLE)
                }

                2 -> {
                    connect(R.id.tvCurrentIssueLeftTime, ConstraintSet.TOP, R.id.layoutCurrentIssueInfo, ConstraintSet.TOP)
                    connect(R.id.tvCurrentIssueLeftTime, ConstraintSet.START, R.id.layoutCurrentIssueInfo, ConstraintSet.START)
                    connect(R.id.tvCurrentIssueLeftTime, ConstraintSet.BOTTOM, R.id.layoutCurrentIssueInfo, ConstraintSet.BOTTOM)
                    setMargin(R.id.tvCurrentIssueLeftTime, ConstraintSet.START, dpToPx(30f, requireContext()).toInt())
                }

                3 -> {
                    connect(R.id.tvCurrentIssueNumber, ConstraintSet.START, R.id.tvCurrentIssueLeftTime, ConstraintSet.END)
                    connect(R.id.tvCurrentIssueNumber, ConstraintSet.TOP, R.id.tvCurrentIssueLeftTime, ConstraintSet.TOP)
                    connect(R.id.tvCurrentIssueNumber, ConstraintSet.BOTTOM, R.id.tvCurrentIssueLeftTime, ConstraintSet.BOTTOM)
                    setMargin(R.id.tvCurrentIssueNumber, ConstraintSet.START, dpToPx(30f, requireContext()).toInt())
                }

                4 -> {
                    connect(R.id.btnZoom, ConstraintSet.TOP, R.id.layoutBetMain, ConstraintSet.TOP)
                    connect(R.id.btnZoom, ConstraintSet.END, R.id.layoutBetMain, ConstraintSet.END)
                    setMargin(R.id.btnZoom, ConstraintSet.END, dpToPx(26f, requireContext()).toInt())
                    setMargin(R.id.btnZoom, ConstraintSet.TOP, dpToPx(5f, requireContext()).toInt())
                }
            }

        }
    }
}