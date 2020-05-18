package com.example.page_bet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.base.AppInjector
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_bet.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class BetFragment : Fragment() {

    private lateinit var mViewModel: BetViewModel

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_bet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        mViewModel = AppInjector.obtainViewModel(this)
        btnUp.setOnClickListener { navigation.openBetUp() }
        btnDown.setOnClickListener { navigation.openBetDown() }

        mViewModel.getNewsArticles().observeNotNull(this){ state ->
            when(state) {
                is ViewState.Success -> Log.e("Ian","ViewState.Success")
                is ViewState.Loading -> Log.e("Ian","ViewState.Loading")
                is ViewState.Error -> Log.e("Ian", "ViewState.Error : ${state.message}")
            }
        }


    }

    inline fun <T> LiveData<T>.observeNotNull(owner: LifecycleOwner, crossinline observer: (T) -> Unit) {
        this.observe(owner, Observer { it?.apply(observer) })
    }
}