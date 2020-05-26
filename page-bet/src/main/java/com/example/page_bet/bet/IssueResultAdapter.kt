package com.example.page_bet.bet

import android.util.Log
import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.MultipleIssueResultItem

class IssueResultAdapter(data: MutableList<MultipleIssueResultItem>) :
    BaseMultiItemQuickAdapter<MultipleIssueResultItem, BaseViewHolder>(data) {

    init {
        addItemType(MultipleIssueResultItem.TIME_LOTTERY, R.layout.item_issue_time_lottery)
        addItemType(MultipleIssueResultItem.RACING, R.layout.item_issue_racing)
        addItemType(MultipleIssueResultItem.HURRY_THREE, R.layout.item_issue_hurry_three)
        addItemType(MultipleIssueResultItem.MARX_SIX, R.layout.item_issue_marx_six)
        addItemType(MultipleIssueResultItem.LUCKY, R.layout.item_issue_lucky)
        addItemType(MultipleIssueResultItem.CHOOSE, R.layout.item_issue_choose)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleIssueResultItem?) {
        helper?.let {
            Log.e("Ian", "[IssueResultAdapter] itemViewType:${it.itemViewType}")
            when (it.itemViewType) {
                MultipleIssueResultItem.TIME_LOTTERY -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 5) {
                        data[0].getDataList().let { list ->
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                        }
                    }
                }
                MultipleIssueResultItem.RACING -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 10) {
                        data[0].getDataList().let { list ->
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
                MultipleIssueResultItem.CHOOSE -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 5) {
                        data[0].getDataList().let { list ->
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, list[3])
                            it.setText(R.id.tvPosition5, list[4])
                        }
                    }
                }
                MultipleIssueResultItem.LUCKY -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 3) {
                        data[0].getDataList().let { list ->
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, (list[0].toInt()+list[1].toInt()+list[2].toInt()).toString())
                        }
                    }
                }
                MultipleIssueResultItem.MARX_SIX -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 7) {
                        data[0].getDataList().let { list ->
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
                MultipleIssueResultItem.HURRY_THREE -> {
                    //TODO 骰子圖先跳過
                }
            }
        }

    }


}