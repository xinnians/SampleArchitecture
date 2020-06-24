package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.BetUnitDisplayMode

class MultipleLotteryEntity(var displayMode: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER,
                            var data: BetData,
                            var isFull: Boolean = false): MultiItemEntity {

    companion object{
        const val FULL_SCREEN = 10
    }

    override fun getItemType(): Int = if(isFull)FULL_SCREEN+displayMode.typeNumber else displayMode.typeNumber

}