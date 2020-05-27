package com.example.page_bet.bet_menu

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.GameMenuResponse

class GameListAdapter(data: List<GameMenuResponse.Data.GameInfoEntity>) :
    BaseQuickAdapter<GameMenuResponse.Data.GameInfoEntity, BaseViewHolder>(R.layout.item_game, data) {

    init {
        Log.e("Ian","[GameListAdapter] init. data length:${data.size}")
    }

    override fun convert(helper: BaseViewHolder?, item: GameMenuResponse.Data.GameInfoEntity?) {
//        helper?.setText(R.id.tvGameName,item?.gameName?:"empty")
        helper?.setText(R.id.tvGameName,item?.gameName?:"empty")
        helper?.addOnClickListener(R.id.layoutBackground)
    }
}