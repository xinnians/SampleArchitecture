package com.example.page_main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.carousel_layout_tool.CarouselLayoutManager
import com.example.base.carousel_layout_tool.CarouselZoomPostLayoutListener
import com.example.base.carousel_layout_tool.CenterScrollListener
import com.example.base.carousel_layout_tool.DefaultChildSelectionListener
import com.example.base.navbar_tool.MeowBottomNavigation
import com.example.base.observeNotNull
import com.example.repository.model.base.ViewState
import com.example.page_main.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MainFragment : BaseFragment() {

    companion object {
        /**
         * NavigationBar ID
         */
        private const val ID_NOTICE = 1
        private const val ID_ACTIVIT = 2
        private const val ID_DEPOSIT = 3
        private const val ID_TRANSATION = 4
        private const val ID_USER = 5
        private const val ID_HOME = 6
        /**
         * subCells ID
         */
        private const val ID_SUB_MESSAGE = 7
        private const val ID_SUB_ANNOUNCEMENT = 8
        private const val ID_SUB_CS = 9
    }

    private var isHide = true
    private lateinit var mMainViewModel: MainViewModel

    private val navigation: MainNavigation by lazy {
        XInjectionManager.findComponent<MainNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        setListener()
        val names = arrayOf("彩票", "棋盤", "真人視訊", "百家樂", "麻將")
        val adapter = CarouselAdapter(names)
        initRecyclerView(view.findViewById(R.id.recycle_view), CarouselLayoutManager(CarouselLayoutManager.VERTICAL, false), adapter)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    // 輪播 recyclerView
    private fun initRecyclerView(recyclerView:RecyclerView, layoutManager:CarouselLayoutManager, adapter:CarouselAdapter) {
        // enable zoom effect. this line can be customized
        layoutManager.setPostLayoutListener(CarouselZoomPostLayoutListener())
        layoutManager.maxVisibleItems = 1
        recyclerView.layoutManager = layoutManager
        // we expect only fixed sized item for now
        recyclerView.setHasFixedSize(true)
        // sample adapter with random data
        recyclerView.adapter = adapter
        // enable center post scrolling
        recyclerView.addOnScrollListener(CenterScrollListener())
        // enable center post touching on item and item click listener
        DefaultChildSelectionListener.initCenterItemListener(object: DefaultChildSelectionListener.OnCenterItemClickListener {
            @Override
            override fun onCenterItemClicked(
                recyclerView: RecyclerView,
                carouselLayoutManager: CarouselLayoutManager,
                v: View
            ) {
                // TODO
                var position = recyclerView.getChildAdapterPosition(v)

            }
        }, recyclerView, layoutManager)
        layoutManager.addOnItemSelectionListener(object : CarouselLayoutManager.OnCenterItemSelectionListener{
            @Override
            override fun onCenterItemChanged(adapterPosition: Int) {
                if(CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    //TODO
                }
            }
        })
    }

    private fun init() {
        mMainViewModel = AppInjector.obtainViewModel(this)
        Log.e("[MainFragment]", "lotteryToken: ${getSharedViewModel().lotteryToken.value}")

        mMainViewModel.getGameMenuResult(getSharedViewModel().lotteryToken.value.orEmpty())
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> Log.e("Ian", "ViewState.Success : ${state.data.data}")
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }

            }
        // 領紅包按鈕
        btnRedBag.setOnClickListener {
            //TODO
        }
        // 每日簽到按鈕
        btnDailySign.setOnClickListener {
            //TODO
        }
        // NavitationBar 按鈕
        meowNavBar.let {
            it.add(MeowBottomNavigation.Model(ID_NOTICE, R.drawable.ic_notice,
                arrayListOf(
                    MeowBottomNavigation.Model(ID_SUB_MESSAGE, R.drawable.ic_message),
                    MeowBottomNavigation.Model(ID_SUB_ANNOUNCEMENT, R.drawable.ic_announcement),
                    MeowBottomNavigation.Model(ID_SUB_CS, R.drawable.ic_cs)
                )))
            it.add(MeowBottomNavigation.Model(ID_ACTIVIT, R.drawable.ic_activity))
            it.add(MeowBottomNavigation.Model(ID_DEPOSIT, R.drawable.ic_deposit))
            it.add(MeowBottomNavigation.Model(ID_TRANSATION, R.drawable.ic_transaction))
            it.add(MeowBottomNavigation.Model(ID_USER, R.drawable.ic_user))
            it.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home))
            // navBar icon 加入 badgeNumber
            it.setCount(ID_NOTICE, "222")
            // subCells icon 加入 badgeNumber
            it.setSubItemBadgeDraw(ID_SUB_MESSAGE, "")
            // 初始設定自動彈出第一個 NavBar icon
            it.show(ID_NOTICE)
            it.setOnShowListener {
                //TODO
            }
            it.setOnClickMenuListener {
                when(it.id) {
                    ID_ACTIVIT -> navigation.goToBetMenuPage()
                }
            }
            it.setOnReselectListener {  }
            it.setCellOnClickListener(object : MeowBottomNavigation.CellOnClickListener {
                override fun cellOnClickListener(id: Int) {
                    //TODO
                }
            })
        }

    }

    private fun setListener() {
//        ctbTitleBar.let {
//            it.money = "1,000,000.000"
//            it.showTime = 5000
//            it.showBack = true
//            it.backListener(View.OnClickListener { navigation.backToLoginPage() }
//
//            )
//        }
    }
}
