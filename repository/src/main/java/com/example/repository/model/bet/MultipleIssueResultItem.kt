package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.GameTypeId

open class MultipleIssueResultItem : MultiItemEntity {

    private var itemType = GameTypeId.TIME_LOTTERY.typeId
    var data: LastIssueResultResponse.Data? = null

    constructor(itemType: Int = GameTypeId.TIME_LOTTERY.typeId, data: LastIssueResultResponse.Data){
        this.itemType = itemType
        this.data = data
    }

    override fun getItemType(): Int = itemType

    fun getDataList(): List<String> = data?.winNum?.split(",") ?: listOf()
}