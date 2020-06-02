package com.example.page_bet.bet.lottery_record

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleHistoryRecord

class HistoryRecordAdapter(data: MutableList<MultipleHistoryRecord>): BaseMultiItemQuickAdapter<MultipleHistoryRecord, BaseViewHolder>(data) {

    init {
        addItemType(MultipleHistoryRecord.TIME_LOTTERY, R.layout.item_history_time_lottery)
        addItemType(MultipleHistoryRecord.RACING, R.layout.item_history_racing)
        addItemType(MultipleHistoryRecord.HURRY_THREE, R.layout.item_history_hurry_three)
        addItemType(MultipleHistoryRecord.MARX_SIX, R.layout.item_history_marx_six)
        addItemType(MultipleHistoryRecord.LUCKY, R.layout.item_history_lucky)
        addItemType(MultipleHistoryRecord.CHOOSE, R.layout.item_history_choose)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleHistoryRecord?) {
        helper?.let {
            Log.e("Ian", "[HistoryRecordAdapter] itemViewType:${it.itemViewType}")
            it.setText(R.id.tvIssueNumber,item?.data?.issueNum)
            when (it.itemViewType) {
                MultipleHistoryRecord.TIME_LOTTERY -> {
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
                MultipleHistoryRecord.RACING -> {
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
                MultipleHistoryRecord.CHOOSE -> {
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
                MultipleHistoryRecord.LUCKY -> {
                    item?.data?.winNum?.split(",")?.let { list ->
                        if(list.size>=4){
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, (list[0].toInt()+list[1].toInt()+list[2].toInt()).toString())
                        }
                    }
                }
                MultipleHistoryRecord.MARX_SIX -> {
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
                MultipleHistoryRecord.HURRY_THREE -> {
                    //TODO 骰子圖先跳過
                }
                else -> {

                }
            }
        }
    }
}