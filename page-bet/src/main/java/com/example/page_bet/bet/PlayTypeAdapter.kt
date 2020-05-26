package com.example.page_bet.bet

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.PlayTypeInfoEntity

class PlayTypeAdapter(data: List<PlayTypeInfoEntity>) :
    BaseQuickAdapter<PlayTypeInfoEntity, BaseViewHolder>(R.layout.item_play_type, data) {
    override fun convert(helper: BaseViewHolder?, item: PlayTypeInfoEntity?) {
        helper?.let {
            it.setText(R.id.tvPlayType,item?.displayName)
            it.addOnClickListener(R.id.tvPlayType)
        }
    }
}