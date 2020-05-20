package com.example.page_bet.game_favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.page_bet.R

class GameFavoriteFragment : BaseFragment() {

    private lateinit var mGameFavoriteViewModel: GameFavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_game_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

    }

    private fun init(){
        mGameFavoriteViewModel = AppInjector.obtainViewModel(this)
        mGameFavoriteViewModel.getFavoriteList(getSharedViewModel().gameMenuList.value)
    }
}