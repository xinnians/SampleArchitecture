package com.example.repository.model.bet

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.example.repository.constant.GameTypeId

open class MultipleHistoryRecord(var type: Int = GameTypeId.TIME_LOTTERY.typeId,
                                 var data: HistoricalResponse.Data) : MultiItemEntity {


    override fun getItemType(): Int = type
}