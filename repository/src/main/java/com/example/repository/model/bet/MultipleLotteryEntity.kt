package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.BetItemType
import com.example.repository.constant.BetUnitDisplayMode

class MultipleLotteryEntity(var itemType: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER,var data: BetData,var isFull: Boolean = false): MultiItemEntity {

    override fun getItemType(): Int = if(isFull)10+itemType.typeNumber else itemType.typeNumber

}