package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.BetItemType

class MultipleLotteryEntity: MultiItemEntity {

    private var itemType = BetItemType.NONE
    private var data: BetData? = null

    constructor(itemType: BetItemType = BetItemType.NONE, data: BetData){
        this.itemType = itemType
    }

    override fun getItemType(): Int = itemType.typeNumber

    fun getData() : BetData? = data
}