package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity

class MultiplePlayTypePositionItem : MultiItemEntity {

    private var itemLength: Int = 0
    private var data: BetData? = null

    constructor(itemLength: Int, data: BetData) {
        this.itemLength = itemLength
        this.data = data
    }

    override fun getItemType(): Int = itemLength

    fun getData(): BetData? = data
}