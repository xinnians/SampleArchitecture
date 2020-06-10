package com.example.page_bet.cart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.inflate
import com.example.base.observeNotNull
import com.example.page_bet.R
import com.example.page_bet.bet.BetViewModel
import com.example.repository.model.base.ViewState
import com.example.repository.room.Cart
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_cart_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartListFragment : BaseFragment() {
    private lateinit var cartListAdapter: CartListAdapter
    private lateinit var mViewModel: BetViewModel
    private var gameList = mutableListOf<Cart>()
    private var gameId = -1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = container?.inflate(R.layout.fragment_cart_list)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.let {
            gameId = it!!.getInt("gameId")
        }
        init()
    }

    private fun init() {
        Log.e("Mori", "[CartListFragment] init")
        mViewModel = AppInjector.obtainViewModel(this)
        mViewModel.getCartList(gameId).observeNotNull(this) { state ->
            when (state) {
                is ViewState.Success -> {
                    Log.e("Mori", "ViewState.Success")
                    gameList = state.data
                    cartListAdapter = CartListAdapter(gameList)
                    rvCartList.let {
                        it.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
                        it.adapter = cartListAdapter
                    }
                }
                is ViewState.Loading -> Log.e("Mori", "ViewState.Loading")
                is ViewState.Error -> Log.e("Mori", "ViewState.Error : ${state.message}")
            }
        }
    }

}