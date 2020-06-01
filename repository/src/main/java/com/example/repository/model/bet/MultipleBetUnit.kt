package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.BetItemType
import com.example.repository.constant.BetUnitDisplayMode

class MultipleBetUnit(
    var itemType: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER,
    var data: BetUnit? = null
) : MultiItemEntity {
    override fun getItemType(): Int = itemType.typeNumber
}