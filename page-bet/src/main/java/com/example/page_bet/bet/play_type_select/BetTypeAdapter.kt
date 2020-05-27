package com.example.page_bet.bet.play_type_select

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.BetTypeEntity

class BetTypeAdapter(data: List<BetTypeEntity>) :
    BaseQuickAdapter<BetTypeEntity, BaseViewHolder>(R.layout.item_bet_type, data) {
    override fun convert(helper: BaseViewHolder?, item: BetTypeEntity?) {
        helper?.let {
            it.setText(R.id.tvBetTypeName, item?.betTypeDisplayName)
                .addOnClickListener(R.id.tvBetTypeName)
                .setVisible(R.id.viewBackground,item?.isSelect == true)
        }
    }
}