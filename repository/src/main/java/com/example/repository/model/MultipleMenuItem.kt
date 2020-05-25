package com.example.repository.model

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.model.GameMenuResponse

open class MultipleMenuItem : MultiItemEntity {

    companion object {
        const val FAVORITE = 1
        const val HOT = 2
        const val NORMAL = 3
    }

    private var itemType = 3
    private var data: GameMenuResponse.Data? = null

    constructor(itemType: Int = 3,data: GameMenuResponse.Data) {
        this.itemType = itemType
        this.data = data
    }

    override fun getItemType(): Int = itemType

    fun getData(): GameMenuResponse.Data? = data

}