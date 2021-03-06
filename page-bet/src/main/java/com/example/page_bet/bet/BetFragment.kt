package com.example.page_bet.bet

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Transition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.viewModelScope
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
import com.example.page_bet.cart.CartViewModel
import com.example.repository.constant.playTypeID_206010
import com.example.repository.model.base.ViewState
import com.example.repository.model.bet.*
import com.example.repository.room.Cart
import kotlinx.android.synthetic.main.fragment_bet.*
import kotlinx.android.synthetic.main.fragment_bet.toolbar
import kotlinx.android.synthetic.main.fragment_lottery_center.*
import kotlinx.coroutines.isActive
import me.vponomarenko.injectionmanager.x.XInjectionManager

class BetFragment : BaseFragment() {

    //TODO 這頁面要判斷當下手機是否為16:9的比例來切換顯示Layout

    companion object {
        const val TAG_GAME_ID = "tag_game_id"
        const val TAG_GAME_NAME = "tag_game_name"
        const val TAG_GAME_TYPE = "tag_game_type"
        const val TAG_GAME_IN_CART = "tag_game_in_cart"
    }

    private lateinit var mViewModel: BetViewModel
    private lateinit var cartViewModel: CartViewModel
    private var mPlayTypeDialog: PlayTypeDialog? = null
    private var mLotteryHistoryDialog: LotteryRecordDialog? = null

    private var mBetPositionAdapter: BetPositionAdapter? = null
    private var mBetRegionAdapter: BetRegionAdapter? = null
    private var mIssueResultAdapter: IssueResultAdapter? = null

    private var isBetUnitShow = true
    private var isZoomIn = false
    private var isGoToShoppingCart = false
    private var isGameInCart = false
    private var isBigScreen = true

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
        initScreen()
    }

    private fun initData() {
        mViewModel = AppInjector.obtainViewModel(this)
        cartViewModel = AppInjector.obtainViewModel(this)
        arguments?.let {
            mViewModel.mGameName = it.getString(TAG_GAME_NAME, "empty")
            ivGameName.text = mViewModel.mGameName
            mViewModel.mGameId = it.getInt(TAG_GAME_ID, -1)
            mViewModel.mGameTypeId = it.getInt(TAG_GAME_TYPE, -1)
        }
        cartViewModel.checkGameInCart(mViewModel.mGameId)
        cartViewModel.getAllCartList()
        isBigScreen = getSharedViewModel().isBigScreen.value ?: true
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

        viewBetMultipleSelector.setOnMultipleValueChangeListener(object : BetMultipleSelector.OnMultipleValueChangeListener {
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
//            var para: BetEntityParam = BetEntityParam(mViewModel.mIssueId,
//                                                      arrayListOf(BonusOrderEntity(betCurrency = 1,
//                                                                                   betUnit = 1.0,
//                                                                                   multiple = 1,
//                                                                                   rebate = 0.0,
//                                                                                   uuid = "uuid",
//                                                                                   amount = 1,
//                                                                                   playTypeCode = mViewModel.mPlayTypeId,
//                                                                                   betNumber = mViewModel.mSelectNumber,
//                                                                                   betCount = 10000)))
//            Log.e("Ian", "[getBetList] param: $para")
//            mViewModel.getBetList(getSharedViewModel().lotteryToken.value ?: "empty", para).observeNotNull(this) { state ->
//                when (state) {
//                    is ViewState.Success -> {
//                        Log.e("Ian", "[getBetList] ViewState.success: data:${state.data}")
//                    }
//                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
//                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
//                }
//            }
        }

        //上層所有資訊欄位
        val zoomIn = ConstraintSet()
        val zoomOut = ConstraintSet()
        //期數欄位
        val zoomInTopRow = ConstraintSet()
        val zoomOutTopRow = ConstraintSet()
        //最外層
        val mainInLayout = ConstraintSet()
        val mainOutLayout = ConstraintSet()
        //投注單位
        val showRate = ConstraintSet()
        showRate.clone(clUnderLayout)
        mainInLayout.clone(layoutBetMain)
        mainOutLayout.clone(layoutBetMain)
        zoomIn.clone(clTopLayout)
        zoomOut.clone(clTopLayout)
        zoomInTopRow.clone(layoutCurrentIssueInfo)
        zoomOutTopRow.clone(layoutCurrentIssueInfo)

        clZoom.onClick {
            if (isZoomIn) {
                //恢復原來大小
                val zoomOutTransition = AutoTransition()
                zoomOutTransition.addListener(object : Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {
                        var layout = ScrollableLinearLayoutManager(context, false)
                        layout.orientation = LinearLayoutManager.VERTICAL
                        rvBetRegion?.layoutManager = layout

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
                ivZoom.setImageDrawable(context!!.drawable(R.drawable.ic_icon_extend))
                if (isBigScreen) {
                    showRate.applyTo(clUnderLayout)
                    tvLabel.gone()
                } else {
                    tvLabel.visible()
                }
                isZoomIn = false
            } else {
                //放大投注區
                val zoomInTransition = AutoTransition()
                zoomInTransition.addListener(object : Transition.TransitionListener {
                    override fun onTransitionEnd(transition: Transition?) {

                        var layout = ScrollableLinearLayoutManager(context, true)
                        layout.orientation = LinearLayoutManager.VERTICAL
                        rvBetRegion?.layoutManager = layout

                        var list: ArrayList<MultipleLotteryEntity> = arrayListOf()
                        mBetPositionAdapter?.data?.apply {
                            for(index in 0 until this.size){
                                if(mViewModel.mPlayTypeId.toString() == playTypeID_206010 && index == 1){
                                    list.add(MultipleLotteryEntity(mViewModel.mSecondBetItemType.unitDisplayMode,
                                                                   this[index].getData()!!,
                                                                   true))
                                }else{
                                    list.add(MultipleLotteryEntity(mViewModel.mBetItemType.unitDisplayMode,
                                                                   this[index].getData()!!,
                                                                   true))
                                }
                            }
                        }
                        mBetPositionAdapter?.data?.forEach {

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

                zoomInView(mainInLayout, R.id.clZoom, 4).applyTo(layoutBetMain)
                if (isBigScreen) {
                    tvLabel.visible()
                    hideRateLayout(layoutBetMain)
                }
                ivZoom.setImageDrawable(context!!.drawable(R.drawable.ic_icon_narrow))
                isZoomIn = true
            }
        }

        tvLabel.onClick {
            if (isBetUnitShow) {
                hideRateLayout(clUnderLayout)
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
            cartViewModel.addCart(cart)
        }

        ivShoppingCart.onClick {
            if (isGoToShoppingCart) {
                navigation.toShoppingCartPage(Bundle().apply {
                    putBoolean(TAG_GAME_IN_CART, isGameInCart)
                    putInt(TAG_GAME_ID, mViewModel.mGameId)
                })
            } else {
                Toast.makeText(requireContext(), "購物車沒資料喔", Toast.LENGTH_SHORT).show()
            }
        }

        toolbar.backListener(View.OnClickListener { navigation.backPrePage() })
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

        cartViewModel.addCartResult.observeNotNull(this){
            if (-1L != it) {
//                Toast.makeText(requireContext(), "加入購物車完成", Toast.LENGTH_SHORT).show()
                isGameInCart = true
            }
        }

        cartViewModel.getAllCartListResult.observeNotNull(this) {
            isGoToShoppingCart = it
        }

        mViewModel.liveIsNeedShowFullScreen.observeNotNull(this) {
            clZoom.visibility = if (it) View.INVISIBLE else View.VISIBLE
        }

        cartViewModel.checkCartListResult.observeNotNull(this) { result ->
            isGameInCart = if (result.size > 0) {
                viewPoint.visible()
                true
            } else {
                viewPoint.gone()
                false
            }

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

    private fun initScreen() {
        if (isBigScreen) {
            tvLabel.gone()
        } else {
            tvLabel.visible()
            hideRateLayout(clUnderLayout)
        }
    }

    private fun hideRateLayout(connectLayout: ViewGroup) {
        val rateSet = ConstraintSet()
        TransitionManager.beginDelayedTransition(connectLayout)
        rateSet.apply {
            clear(R.id.clRateLayout)
            connect(R.id.clRateLayout, ConstraintSet.TOP, R.id.clBottomLayout, ConstraintSet.TOP)
            connect(R.id.clRateLayout, ConstraintSet.START, R.id.clBottomLayout, ConstraintSet.START)
            connect(R.id.clRateLayout, ConstraintSet.END, R.id.clBottomLayout, ConstraintSet.END)
        }.applyTo(clUnderLayout)
        tvLabel.text = "Show"
        isBetUnitShow = false
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
                    connect(resId, ConstraintSet.TOP, R.id.layoutCurrentIssueInfo, ConstraintSet.TOP)
                    connect(resId, ConstraintSet.START, R.id.layoutCurrentIssueInfo, ConstraintSet.START)
                    connect(resId, ConstraintSet.BOTTOM, R.id.layoutCurrentIssueInfo, ConstraintSet.BOTTOM)
                    setMargin(resId, ConstraintSet.START, dpToPx(30f, requireContext()).toInt())
                }

                3 -> {
                    connect(resId, ConstraintSet.START, R.id.tvCurrentIssueLeftTime, ConstraintSet.END)
                    connect(resId, ConstraintSet.TOP, R.id.tvCurrentIssueLeftTime, ConstraintSet.TOP)
                    connect(resId, ConstraintSet.BOTTOM, R.id.tvCurrentIssueLeftTime, ConstraintSet.BOTTOM)
                    setMargin(resId, ConstraintSet.START, dpToPx(30f, requireContext()).toInt())
                }

                4 -> {
                    connect(resId, ConstraintSet.TOP, R.id.layoutBetMain, ConstraintSet.TOP)
                    connect(resId, ConstraintSet.END, R.id.layoutBetMain, ConstraintSet.END)
                    setMargin(resId, ConstraintSet.END, dpToPx(26f, requireContext()).toInt())
                    setMargin(resId, ConstraintSet.TOP, dpToPx(5f, requireContext()).toInt())
                }

                5 -> {
                    connect(resId, ConstraintSet.START, R.id.layoutBetMain, ConstraintSet.START)
                    connect(resId, ConstraintSet.END, R.id.layoutBetMain, ConstraintSet.END)
                    connect(resId, ConstraintSet.BOTTOM, R.id.clBottomLayout, ConstraintSet.TOP)
                }

            }

        }
    }

}
