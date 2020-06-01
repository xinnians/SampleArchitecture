package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.BetItemType
import com.example.repository.constant.BetUnitDisplayMode

class MultipleLotteryEntity: MultiItemEntity {

    private var itemType = BetUnitDisplayMode.ONLY_NUMBER
    private var data: BetData? = null

    constructor(itemType: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER, data: BetData){
        this.itemType = itemType
        this.data = data
    }

    override fun getItemType(): Int = itemType.typeNumber

    fun getData() : BetData? = data
}