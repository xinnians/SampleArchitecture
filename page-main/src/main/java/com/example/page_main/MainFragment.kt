package com.example.page_main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.base.AppInjector
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import com.example.base.carousel_layout_tool.CarouselLayoutManager
import com.example.base.carousel_layout_tool.CarouselZoomPostLayoutListener
import com.example.base.carousel_layout_tool.CenterScrollListener
import com.example.base.carousel_layout_tool.DefaultChildSelectionListener
import com.example.base.observeNotNull
import com.example.repository.model.base.ViewState
import com.example.page_main.adapter.CarouselAdapter
import kotlinx.android.synthetic.main.fragment_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager


class MainFragment : BaseFragment() {

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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    private fun init() {
        (activity as BaseActivity?)?.showBottomNav()
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
        val names = arrayOf("彩票", "棋盤", "真人視訊", "百家樂", "麻將")
        val adapter = CarouselAdapter(names)
        initRecyclerView(
            recycle_view,
            CarouselLayoutManager(CarouselLayoutManager.VERTICAL, false),
            adapter
        )
    }

    private fun setListener() {
        // 領紅包按鈕
        btnRedBag.setOnClickListener {
            //TODO
        }
        // 每日簽到按鈕
        btnDailySign.setOnClickListener {
            //TODO
        }
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
                // TODO 中間被點選的項目
                var position = recyclerView.getChildAdapterPosition(v)
                Log.d("msg", "click item: ${adapter.getItem(position)}")
                when(position) {
                    // 彩票
                    0 -> navigation.goToBetMenuPage()
                    // 棋盤
                    1 -> {}
                }
            }
        }, recyclerView, layoutManager)
        layoutManager.addOnItemSelectionListener(object : CarouselLayoutManager.OnCenterItemSelectionListener{
            @Override
            override fun onCenterItemChanged(adapterPosition: Int) {
                //TODO 滑動到中間的項目
                if(CarouselLayoutManager.INVALID_POSITION != adapterPosition) {
                    Log.d("msg", "invalid item: ${adapter.getItem(adapterPosition)}")
                }
            }
        })
    }
}
