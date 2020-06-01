package com.example.page_main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.navbar_tool.MeowBottomNavigation
import com.example.base.observeNotNull
import com.example.repository.model.base.ViewState
import kotlinx.android.synthetic.main.fragment_main.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class MainFragment : BaseFragment() {

    companion object {
        /**
         * NavigationBar ID
         */
        private const val ID_HOME = 1
        private const val ID_EXPLORE = 2
        private const val ID_MESSAGE = 3
        private const val ID_ACCOUNT = 4
        private const val ID_NOTIFICATION = 5
        private const val ID_NOTIFICATION_1 = 6
        /**
         * subCells ID
         */
        private const val ID_SUB_HOME = 7
        private const val ID_SUB_EXPLORE = 8
        private const val ID_SUB_MESSAGE = 9
        private const val ID_SUB_NOTIFICATION = 10
    }

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
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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

        Log.e("[MainFragment]","lotteryToken: ${getSharedViewModel().lotteryToken.value}")
        meowNavBar.let {
            it.add(MeowBottomNavigation.Model(ID_HOME, R.drawable.ic_home,
                arrayListOf(
                    MeowBottomNavigation.Model(ID_SUB_MESSAGE, R.drawable.ic_message),
                    MeowBottomNavigation.Model(ID_SUB_EXPLORE, R.drawable.ic_explore),
                    MeowBottomNavigation.Model(ID_SUB_NOTIFICATION, R.drawable.ic_notification),
                    MeowBottomNavigation.Model(ID_SUB_HOME, R.drawable.ic_home)
                )))
            it.add(MeowBottomNavigation.Model(ID_ACCOUNT, R.drawable.ic_account))
            it.add(MeowBottomNavigation.Model(ID_EXPLORE, R.drawable.ic_explore))
            it.add(MeowBottomNavigation.Model(ID_MESSAGE, R.drawable.ic_message))
            it.add(MeowBottomNavigation.Model(ID_NOTIFICATION, R.drawable.ic_notification))
            it.add(MeowBottomNavigation.Model(ID_NOTIFICATION_1, R.drawable.ic_notification))
            // navBar icon 加入 badgeNumber
            it.setCount(ID_HOME, "222")
            it.setCount(ID_NOTIFICATION, "")
            // subCells icon 加入 badgeNumber
            it.setSubItemBadgeDraw(ID_SUB_HOME, "222")
            it.setSubItemBadgeDraw(ID_SUB_MESSAGE, "")
            // 初始設定自動彈出第一個 NavBar icon
            it.show(ID_HOME)
            it.setOnShowListener {
                Log.d("msg", "onShowListener")
            }
            it.setOnClickMenuListener {
                when(it.id) {
                    ID_MESSAGE -> navigation.goToBetMenuPage()
                }
            }
            it.setOnReselectListener {  }
            it.setCellOnClickListener(object : MeowBottomNavigation.CellOnClickListener {
                override fun cellOnClickListener(id: Int) {
                    TODO("Not yet implemented")
                }
            })
        }

    }
}