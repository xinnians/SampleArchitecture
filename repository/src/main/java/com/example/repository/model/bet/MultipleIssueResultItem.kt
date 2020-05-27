package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity

open class MultipleIssueResultItem : MultiItemEntity {

    companion object {
        const val RACING = 1
        const val TIME_LOTTERY = 2
        const val HURRY_THREE = 4
        const val LUCKY = 6
        const val CHOOSE = 3
        const val MARX_SIX = 5
    }

    private var itemType =
        TIME_LOTTERY
    private var data: LastIssueResultResponse.Data? = null

    constructor(itemType: Int = TIME_LOTTERY, data: LastIssueResultResponse.Data){
        this.itemType = itemType
        this.data = data
    }

    override fun getItemType(): Int = itemType

    fun getDataList(): List<String> = data?.winNum?.split(",") ?: listOf()
}