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
import com.example.repository.room.Cart
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
        vpCartType.isUserInputEnabled = false
        getIdAndName()
    }

    private fun getIdAndName() {
        mViewModel.getAllGameId().observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    allGameId = state.data
                    Log.d("Mori","[allGameId] $allGameId")
                    val tabName = mutableListOf<String>()
                    getSharedViewModel().gameMenuList.value.let { menuItem ->
                        Log.d("Mori","[menuItem] $menuItem")
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
                    setAdapter(allGameId as MutableList<Int>, tabName)
                }

                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
            }
        }
    }

    private fun setAdapter(gameId: MutableList<Int>, tabName: MutableList<String>) {
        val dialogCallback = object : CartPageDialog.SetCallback{
            override fun onCall(view: View, cart: Cart, position: Int) {
                when (view.id) {
                    R.id.clDel -> {
                        mViewModel.delCart(cart).observeNotNull(this@CartFragment) { state ->
                            when (state) {
                                is ViewState.Success -> {
                                    if(-1 != state.data) {
                                        cartPagerAdapter.data?.forEach {cartList ->
                                            if(cartList.contains(cart)){
                                                cartList.remove(cart)
                                            }
                                        }
                                        cartPagerAdapter.notifyDataSetChanged()
                                    }
                                }
                                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
                            }
                        }
                    }

                    R.id.clEdit -> {
                        mViewModel.updateCart(cart).observeNotNull(this@CartFragment) { state ->
                            when (state) {
                                is ViewState.Success -> {
                                    if(-1 != state.data) {
                                        cartPagerAdapter.notifyDataSetChanged()
                                    }
                                }
                                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
                            }
                        }
                    }

                    R.id.clAppend -> {
                        mViewModel.updateCart(cart).observeNotNull(this@CartFragment) { state ->
                            when (state) {
                                is ViewState.Success -> {
                                    if(-1 != state.data) {
                                        cartPagerAdapter.notifyDataSetChanged()
                                    }
                                }
                                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
                            }
                        }
                    }
                }
            }
        }

        cartPagerAdapter = CartPagerAdapter(mutableListOf(), dialogCallback)
        mViewModel.getCartArray(gameId as ArrayList<Int>).observeNotNull(this){ state ->
            when (state) {
                is ViewState.Success -> {
                        cartPagerAdapter.addData(state.data)
                        cartPagerAdapter.notifyDataSetChanged()

                }
                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
            }
        }

        vpCartType.adapter = cartPagerAdapter
        cartPagerAdapter.notifyDataSetChanged()

        TabLayoutMediator(tlCartType, vpCartType) { tab: TabLayout.Tab, i: Int ->
            Log.e("Mori", "add tab text position = $i cartPagerAdapter2: ${cartPagerAdapter.itemCount}")
            tab.text = tabName[i]
        }.attach()

    }


}