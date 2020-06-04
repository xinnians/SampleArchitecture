package com.example.page_bet.bet

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.GameTypeId
import com.example.repository.model.bet.MultipleIssueResultItem

class IssueResultAdapter(data: MutableList<MultipleIssueResultItem>) :
    BaseMultiItemQuickAdapter<MultipleIssueResultItem, BaseViewHolder>(data) {

    init {
        addItemType(GameTypeId.TIME_LOTTERY.typeId, R.layout.item_issue_time_lottery)
        addItemType(GameTypeId.RACING.typeId, R.layout.item_issue_racing)
        addItemType(GameTypeId.HURRY_THREE.typeId, R.layout.item_issue_hurry_three)
        addItemType(GameTypeId.MARX_SIX.typeId, R.layout.item_issue_marx_six)
        addItemType(GameTypeId.LUCKY.typeId, R.layout.item_issue_lucky)
        addItemType(GameTypeId.CHOOSE.typeId, R.layout.item_issue_choose)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleIssueResultItem?) {
        helper?.let {
            Log.e("Ian", "[IssueResultAdapter] itemViewType:${it.itemViewType}")
            when (it.itemViewType) {
                GameTypeId.TIME_LOTTERY.typeId -> {
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
                GameTypeId.RACING.typeId -> {
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
                GameTypeId.CHOOSE.typeId -> {
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
                GameTypeId.LUCKY.typeId -> {
                    if (data.size >= 1 && data[0].getDataList().size >= 3) {
                        data[0].getDataList().let { list ->
                            it.setText(R.id.tvPosition1, list[0])
                            it.setText(R.id.tvPosition2, list[1])
                            it.setText(R.id.tvPosition3, list[2])
                            it.setText(R.id.tvPosition4, (list[0].toInt()+list[1].toInt()+list[2].toInt()).toString())
                        }
                    }
                }
                GameTypeId.MARX_SIX.typeId -> {
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
                GameTypeId.HURRY_THREE.typeId -> {
                    //TODO 骰子圖先跳過
                }
            }
        }

    }


}