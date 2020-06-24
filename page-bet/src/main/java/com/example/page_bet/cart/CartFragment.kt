package com.example.page_bet.cart

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.core.view.forEach
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.*
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.page_bet.bet.BetFragment
import com.example.repository.room.Cart
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart.clAppendLayout
import kotlinx.android.synthetic.main.item_cart_layout.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class CartFragment : BaseFragment() {
    private lateinit var cartPagerAdapter: CartPagerAdapter
    private lateinit var cartAppendListAdapter: CartAppendListAdapter
    private lateinit var cartViewModel: CartViewModel
    private var currentItemId:Int = -1
    private var isAppendListMode = false
    private var isAppendWinStop = false
    private var isGameInCart = false
    private var tabName = mutableListOf<String>()
    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        container?.inflate(R.layout.fragment_cart)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cartViewModel = AppInjector.obtainViewModel(this)
        arguments?.let {
            currentItemId = it.getInt(BetFragment.TAG_GAME_ID)
            isGameInCart = it.getBoolean(BetFragment.TAG_GAME_IN_CART)
        }
        vpCartType.isUserInputEnabled = false
        cartViewModel.getAllGameId()
        cartPagerAdapter = CartPagerAdapter(mutableListOf(), callBack)
        init()
        initListener()
    }

    private fun initListener() {
        btnBet.onClick {
            oneTouchBet()
        }

        ivBackToBet.onClick {
            if (isAppendListMode) {
                Log.d("mori", "is append list = $isAppendListMode")
                (activity as BaseActivity).Dialog(layout = R.layout.alert_big,
                                                  title = "",
                                                  content = "您還有未投注計劃，\n確認重選注單？",
                                                  positiveButton = "重選",
                                                  negativeButton = "返回",
                                                  onNegativeButtonClicked = {},
                                                  onPositiveButtonClicked = {
                                                      getBackToCartList()
                                                  })
            } else {
                Log.d("mori", "is append list = $isAppendListMode")
                navigation.goBackToBetPage()
            }
        }
    }

    private fun init() {
        cartViewModel.let {
            it.allGameIdResult.observeNotNull(this) { result ->
                getIdAndName(result)
            }

            it.delCartResult.observeNotNull(this) { result ->
                cartPagerAdapter.addData(result)
            }

            it.updateCartResult.observeNotNull(this) { result ->
                if (-1 != result) {
                    cartPagerAdapter.notifyDataSetChanged()
                }
            }

            it.getCartListResult.observeNotNull(this) { result ->
                cartPagerAdapter.addData(result)
            }

            it.delCartByIdResult.observeNotNull(this) { result ->
                if (-1 != result) {
                    Log.d("mori", "remove by id is done")
                }
            }
        }
    }

    private fun setOnKeyDown() {
        view?.let {
            it.isFocusableInTouchMode = true
            it.requestFocus()
            it.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (isAppendListMode) {
                            (activity as BaseActivity).Dialog(layout = R.layout.alert_big,
                                                              title = "",
                                                              content = "您還有未投注計劃，\n確認重選注單？",
                                                              positiveButton = "重選",
                                                              negativeButton = "返回",
                                                              onNegativeButtonClicked = {},
                                                              onPositiveButtonClicked = {
                                                                  getBackToCartList()
                                                              })
                            return@setOnKeyListener true
                        }
                    }
                }
                return@setOnKeyListener false
            }
        }
    }

    private fun getBackToCartList() {
        clCartListLayout.visible()
        clAppendLayout.gone()
        clPencil.visible()
        clBin.gone()
        clBack.gone()
        isAppendListMode = false
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
        setOnKeyDown()
        var appendCount = -1
        cart.let {
            tvAppendBetNumber.text = it.betNumber
            tvAppendAmount.text = it.amount.toString()
            tvAppendBetCount.text = it.betCount.toString()
        }

        setting.let {
            appendCount = it.get("appendCount").asInt
            tvAppendCount.text = appendCount.toString()
            isAppendWinStop = it.get("isWinStop").asBoolean

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
        }

        rbAppendWinStop.isChecked = isAppendWinStop
        rbAppendWinStop.onClick {
            if (!isAppendWinStop) {
                isAppendWinStop = true
                rbAppendWinStop.isChecked = isAppendWinStop
            } else {
                isAppendWinStop = false
                rbAppendWinStop.isChecked = isAppendWinStop
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
                if (cartAppendListAdapter.data.size == 0) {
                    getBackToCartList()
                }
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
            if (cartAppendListAdapter.data.size == 0) {
                getBackToCartList()
            }
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

        btnBet.onClick {
            if (isAppendListMode) {
                getBackToCartList()
                cartViewModel.delCart(cart)
            } else {
                oneTouchBet()
            }
        }
    }

    private fun oneTouchBet() {
        val tabPosition = tlCartType.selectedTabPosition
        val cartInfo = cartPagerAdapter.data[tabPosition]
        if (cartInfo.size != 0) {
            cartViewModel.delCartById(cartInfo[0].gameId)
            cartPagerAdapter.let {
                tabName.removeAt(tabPosition)
                it.data.removeAt(tabPosition)
                it.notifyDataSetChanged()
                if (it.data.size == 0) {
                    navigation.goBackToBetPage()
                }
            }
        }
    }

    private fun getIdAndName(allId: MutableList<Int>) {
        getSharedViewModel().gameMenuList.value.let { menuItem ->
            val filterList = menuItem?.filterNot {
                it.getData()?.gameTypeDisplayName == "Hot" || it.getData()?.gameTypeDisplayName == "Favorite"
            }
            allId.forEach { id ->
                filterList!!.forEach { filter ->
                    filter.getData()!!.gameInfoEntityList.forEach { entity ->
                        if (id == entity.gameId) {
                            tabName.add(entity.gameName)
                        }
                    }
                }
            }
        }

        cartViewModel.getCartArray(allId as ArrayList<Int>)
        vpCartType.adapter = cartPagerAdapter
        cartPagerAdapter.notifyDataSetChanged()
        TabLayoutMediator(tlCartType, vpCartType) { tab: TabLayout.Tab, i: Int ->
            tab.text = tabName[i]
        }.attach()

        if (isGameInCart) {
            for (position in 0 until allId.size) {
                if (allId[position] == currentItemId) {
                    Handler().postDelayed({ vpCartType.setCurrentItem(position, false) }, 100)
                }
            }
        }
    }

    data class Append(val appendIssueNo: Int, val multiple: Int, val amount: Int, var isCheck: Boolean = false)

}