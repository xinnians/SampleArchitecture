package com.example.page_bet.game_favorite

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.MultipleMenuItem

class GameFavoriteAdapter(data: MutableList<MultipleMenuItem>) :
    BaseQuickAdapter<MultipleMenuItem, BaseViewHolder>(R.layout.item_game_favorite,data) {
    override fun convert(helper: BaseViewHolder?, item: MultipleMenuItem?) {
        TODO("Not yet implemented")
    }
}