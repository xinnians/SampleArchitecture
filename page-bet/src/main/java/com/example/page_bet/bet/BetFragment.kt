package com.example.page_bet.bet

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.base.AppInjector
import com.example.base.observeNotNull
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import com.example.repository.model.ViewState
import kotlinx.android.synthetic.main.fragment_bet.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class BetFragment : Fragment() {

    companion object{
        const val TAG_GAME_ID = "tag_game_id"
        const val TAG_GAME_NAME = "tag_game_name"
    }

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
        arguments?.getString(TAG_GAME_NAME,"empty").let {
            ivGameName.text = it
        }
    }
}