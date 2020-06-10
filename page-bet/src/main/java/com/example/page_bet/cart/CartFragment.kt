package com.example.page_bet.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.inflate
import com.example.base.observeNotNull
import com.example.page_bet.R
import com.example.page_bet.bet.BetViewModel
import com.example.repository.model.base.ViewState
import kotlinx.android.synthetic.main.fragment_cart.*

class CartFragment : BaseFragment() {
    private lateinit var cartPagerAdapter: CartPagerAdapter
    private lateinit var mViewModel: BetViewModel
    private var allGameId = listOf<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_cart)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = AppInjector.obtainViewModel(this)
        vpCartType.setSwipePagingEnabled(false)
        mViewModel.getAllGameId().observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    Log.e("Mori", "ViewState.Success")
                    allGameId = state.data
                    val tabName = mutableListOf<String>()
                    getSharedViewModel().gameMenuList.value.let { menuItem ->
                        for (id in allGameId) {
                            for (item in menuItem!!) {
                                for (entity in item.getData()!!.gameInfoEntityList) {
                                    if (id == entity.gameId) {
                                        tabName.add(entity.gameName)
                                    }
                                }
                            }
                        }
                    }
                    cartPagerAdapter = CartPagerAdapter(childFragmentManager, allGameId, tabName)
                    vpCartType.adapter = cartPagerAdapter
                    tlCartType.setupWithViewPager(vpCartType)
                }
                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
            }
        }
    }
}