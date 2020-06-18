package com.example.page_bet.cart

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.base.*
import com.example.page_bet.R
import com.example.page_bet.bet.BetViewModel
import com.example.repository.room.Cart
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.item_cart_layout.*

class CartFragment : BaseFragment() {
    private lateinit var cartPagerAdapter: CartPagerAdapter
    private lateinit var cartAppendListAdapter: CartAppendListAdapter
    private lateinit var cartViewModel: CartViewModel
    private var isAppendListMode = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_cart)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = AppInjector.obtainViewModel(this)
        vpCartType.isUserInputEnabled = false
        cartViewModel.getAllGameId()
        cartPagerAdapter = CartPagerAdapter(mutableListOf(), callBack)
        init()
        setOnKeyDown()
    }

    private fun init() {
        cartViewModel.let {
            it.allGameIdResult.observeNotNull(viewLifecycleOwner) { result ->
                getIdAndName(result)
            }

            it.delCartResult.observeNotNull(viewLifecycleOwner) { result ->
                cartPagerAdapter.addData(result)
            }

            it.updateCartResult.observeNotNull(viewLifecycleOwner) { result ->
                if (-1 != result) {
                    cartPagerAdapter.notifyDataSetChanged()
                }
            }

            it.getCartListResult.observeNotNull(viewLifecycleOwner) { result ->
                cartPagerAdapter.addData(result)
            }
        }
    }

    private fun setOnKeyDown() {
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            it.setOnKeyListener { _, keyCode, _ ->
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isAppendListMode) {
                        clCartListLayout.visible()
                        clAppendLayout.gone()
                        isAppendListMode = false
                        return@setOnKeyListener true
                    }
                }
                return@setOnKeyListener false
            }
        }
    }

    private val callBack = object : CartPageDialog.SetCallback {
        override fun onCall(view: View, cart: Cart, position: Int) {
            when (view.id) {
                clDelCart.id -> {
                    cartViewModel.delCart(cart)
                }

                clAppendCart.id, clEditCart.id -> {
                    cartViewModel.updateCart(cart)
                }
            }
        }

        override fun onAppendCall(view: View, cart: Cart, position: Int, setting: JsonObject) {
            setAppendView(cart, setting)
        }
    }

    private fun setAppendView(cart: Cart, setting: JsonObject) {
        clCartListLayout.gone()
        clAppendLayout.visible()
        isAppendListMode = true
        var appendCount = -1
        cart.let {
            tvAppendBetNumber.text = it.betNumber
            tvAppendAmount.text = it.amount.toString()
            tvAppendBetCount.text = it.betCount.toString()
        }

        setting.let {
            appendCount = it.get("appendCount").asInt
            tvAppendCount.text = appendCount.toString()

            rbWinStop.isChecked = it.get("isWinStop").asBoolean
            when (it.get("type").asInt) {
                CartPageDialog.MORE_TYPE_1 -> {
                    tvTypeName.text = "同倍追号"
                }

                CartPageDialog.MORE_TYPE_2 -> {
                    tvTypeName.text = "翻倍追号"
                }

                CartPageDialog.MORE_TYPE_3 -> {
                    tvTypeName.text = "利润追号"
                }
            }

            val appendList = mutableListOf<Append>()
            for (i in 0 until appendCount) {
                appendList.add(Append(i + 1, setting.get("multiple").asInt, cart.amount))
            }

            cartAppendListAdapter = CartAppendListAdapter(appendList, object : CartAppendDialog.SetCallback {
                override fun onCall(append: Append, position: Int) {
                    val tempList = cartAppendListAdapter.data
                    if (tempList.contains(append)) {
                        tempList.remove(append)
                    }
                    cartAppendListAdapter.notifyDataSetChanged()
                }
            })

            rvAppendList.let {
                it.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
                it.adapter = cartAppendListAdapter
            }


            clPencil.onClick {
                cartAppendListAdapter.let {
                    it.setShowBox()
                    it.notifyDataSetChanged()
                }

                clPencil.gone()
                clBin.visible()
                clBack.visible()
            }

            clBin.onClick {
                val tempList = cartAppendListAdapter.data
                cartAppendListAdapter.setNewData(tempList.filter { item -> !item.isCheck })
            }

            clBack.onClick {
                cartAppendListAdapter.let {
                    it.setShowBox()
                    it.notifyDataSetChanged()
                }

                clPencil.visible()
                clBin.gone()
                clBack.gone()
            }

        }
    }

    private fun getIdAndName(allId: MutableList<Int>) {
        val tabName = mutableListOf<String>()
        getSharedViewModel().gameMenuList.value.let { menuItem ->
            for (id in allId) {
                for (item in menuItem!!) {
                    for (entity in item.getData()!!.gameInfoEntityList) {
                        if (id == entity.gameId) {
                            tabName.add(entity.gameName)
                        }
                    }
                }
            }
        }
        setAdapter(allId, tabName)
    }

    private fun setAdapter(gameId: MutableList<Int>, tabName: MutableList<String>) {
        cartViewModel.getCartArray(gameId as ArrayList<Int>)
        vpCartType.adapter = cartPagerAdapter
        cartPagerAdapter.notifyDataSetChanged()

        TabLayoutMediator(tlCartType, vpCartType) { tab: TabLayout.Tab, i: Int ->
            tab.text = tabName[i]
        }.attach()

    }


    data class Append(val appendIssueNo: Int, val multiple: Int, val amount: Int, var isCheck: Boolean = false)


}