package com.example.page_bet.bet.lottery_record

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.GameTypeId
import com.example.repository.model.bet.MultipleHistoryRecord

class HistoryRecordAdapter(data: MutableList<MultipleHistoryRecord>): BaseMultiItemQuickAdapter<MultipleHistoryRecord, BaseViewHolder>(data) {

    init {
        addItemType(GameTypeId.TIME_LOTTERY.typeId, R.layout.item_history_time_lottery)
        addItemType(GameTypeId.RACING.typeId, R.layout.item_history_racing)
        addItemType(GameTypeId.HURRY_THREE.typeId, R.layout.item_history_hurry_three)
        addItemType(GameTypeId.MARX_SIX.typeId, R.layout.item_history_marx_six)
        addItemType(GameTypeId.LUCKY.typeId, R.layout.item_history_lucky)
        addItemType(GameTypeId.CHOOSE.typeId, R.layout.item_history_choose)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleHistoryRecord?) {
        helper?.let {
            Log.e("Ian", "[HistoryRecordAdapter] itemViewType:${it.itemViewType}")
            it.setText(R.id.tvIssueNumber,item?.data?.issueNum)
            when (it.itemViewType) {
                GameTypeId.TIME_LOTTERY.typeId -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=5){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                        }
                    }
                }
                GameTypeId.RACING.typeId -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=10){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                            it.setText(R.id.tvPosition6, list[5])
                            it.setText(R.id.tvPosition7, list[6])
                            it.setText(R.id.tvPosition8, list[7])
                            it.setText(R.id.tvPosition9, list[8])
                            it.setText(R.id.tvPosition10, list[9])
                        }
                    }
                }
                GameTypeId.CHOOSE.typeId -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=5){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                        }
                    }
                }
                GameTypeId.LUCKY.typeId -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=4){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, (list[0].toInt()+list[1].toInt()+list[2].toInt()).toString())
                        }
                    }
                }
                GameTypeId.MARX_SIX.typeId -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=7){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                            it.setText(R.id.tvPosition6, list[5])
                            it.setText(R.id.tvPosition7, list[6])
                        }
                    }
                }
                GameTypeId.HURRY_THREE.typeId -> {
                    //TODO 骰子圖先跳過
                }
                else -> {

                }
            }
        }
    }
}