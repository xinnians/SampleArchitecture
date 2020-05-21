package com.example.page_bet.bet_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.observeNotNull
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_bet_menu.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class BetMenuFragment : BaseFragment() {

    private lateinit var mBetMenuViewModel: BetMenuViewModel

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        mBetMenuViewModel = AppInjector.obtainViewModel(this)

        mBetMenuViewModel.getGameMenuResult(getSharedViewModel().lotteryToken.value.orEmpty())
            .observeNotNull(this) { state ->
                when (state) {
                    is ViewState.Success -> {
                        Log.e("Ian", "ViewState.Success : ${state.data}")
                        getSharedViewModel().gameMenuList.postValue(state.data)

                        var layoutManager = LinearLayoutManager(context)
                        layoutManager.orientation = LinearLayoutManager.VERTICAL
                        rvGameList.layoutManager = layoutManager

                        var adapter: BetMenuAdapter = BetMenuAdapter(state.data)
                        rvGameList.adapter = adapter

                        adapter.setOnItemChildClickListener { adapter, view, position ->
                            when(view.id){
                                R.id.ivEdit -> {
                                    navigation.toGameFavoritePage()
                                }
                                else -> {

                                }
                            }
                        }
                    }
                    is ViewState.Loading -> Log.e("Ian", "ViewState.Loading")
                    is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
                }
            }
    }
}