package com.example.page_bet.bet

import android.util.Log
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.BetItemType
import com.example.repository.model.bet.MultipleLotteryEntity

class BetRegionAdapter(data: List<MultipleLotteryEntity>) :
    BaseMultiItemQuickAdapter<MultipleLotteryEntity, BaseViewHolder>(data) {

    init {
        addItemType(BetItemType.DEFAULT_BET_TYPE.typeNumber, R.layout.item_bet_region_default)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleLotteryEntity?) {
        helper?.let {
            it.setText(R.id.tvTitle, item?.getData()?.displayTitle)
//            if (itemLimitHeight > 0) it.getView<ConstraintLayout>(R.id.layoutBetRegion).layoutParams = ViewGroup.LayoutParams()
        }

    }
}