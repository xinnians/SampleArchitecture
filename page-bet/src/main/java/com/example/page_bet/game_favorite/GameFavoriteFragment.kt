package com.example.page_bet.game_favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.AppInjector
import com.example.base.BaseFragment
import com.example.base.onClick
import com.example.page_bet.BetNavigation
import com.example.page_bet.R
import kotlinx.android.synthetic.main.fragment_game_favorite.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class GameFavoriteFragment : BaseFragment() {

    private lateinit var mGameFavoriteViewModel: GameFavoriteViewModel

    private val navigation: BetNavigation by lazy {
        XInjectionManager.findComponent<BetNavigation>()
    }

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
//        mGameFavoriteViewModel.getFavoriteList(getSharedViewModel().gameMenuList.value)

        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvGameList.layoutManager = layoutManager

        var adapter: GameFavoriteAdapter = GameFavoriteAdapter(mGameFavoriteViewModel.getFavoriteList(getSharedViewModel().gameMenuList.value))
        rvGameList.adapter = adapter

        ivLeftArrow.setOnClickListener { navigation.goBackToBetMenuPage() }
        btnSave.onClick { navigation.goBackToBetMenuPage() }
    }
}