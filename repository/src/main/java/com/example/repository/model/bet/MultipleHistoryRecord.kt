package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity

open class MultipleHistoryRecord(var type: Int = TIME_LOTTERY,
                                 var data: HistoricalResponse.Data) : MultiItemEntity {

    companion object {
        const val RACING = 1
        const val TIME_LOTTERY = 2
        const val HURRY_THREE = 4
        const val LUCKY = 6
        const val CHOOSE = 3
        const val MARX_SIX = 5
    }

    override fun getItemType(): Int = type
}