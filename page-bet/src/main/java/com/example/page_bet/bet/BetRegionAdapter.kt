package com.example.page_bet.bet

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.constant.BetUnitDisplayMode
import com.example.repository.model.bet.MultipleBetUnit
import com.example.repository.model.bet.MultipleLotteryEntity

class BetRegionAdapter(data: List<MultipleLotteryEntity>) :
    BaseMultiItemQuickAdapter<MultipleLotteryEntity, BaseViewHolder>(data) {

    //TODO init BetUnitAdapter for four DisplayMode

    private var unitAdapter: BetUnitAdapter = BetUnitAdapter(listOf())

    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleLotteryEntity?) {
        helper?.let {
            Log.e("Ian", "[BetRegionAdapter] convert item:${item?.getData()}")
            it.setText(R.id.tvTitle, item?.getData()?.displayTitle ?: "empty")
            when (it.itemViewType) {
                BetUnitDisplayMode.ONLY_NUMBER.typeNumber -> {
                    it.getView<RecyclerView>(R.id.rvBetUnit).let { recyclerView ->
                        recyclerView.layoutManager = GridLayoutManager(mContext, 5)
                        recyclerView.adapter = unitAdapter
                        var resultList: ArrayList<MultipleBetUnit> = arrayListOf()
                        item?.getData()?.unitList?.forEach { betUnit ->
                            resultList.add(MultipleBetUnit(BetUnitDisplayMode.ONLY_NUMBER, betUnit))
                        }
                        unitAdapter.setNewData(resultList)
                    }
                }
                BetUnitDisplayMode.ONE_CHAR.typeNumber -> {
                    it.getView<RecyclerView>(R.id.rvBetUnit).let { recyclerView ->
                        recyclerView.layoutManager = GridLayoutManager(mContext, 2)
                        recyclerView.adapter = unitAdapter
                        var resultList: ArrayList<MultipleBetUnit> = arrayListOf()
                        item?.getData()?.unitList?.forEach { betUnit ->
                            resultList.add(MultipleBetUnit(BetUnitDisplayMode.ONE_CHAR, betUnit))
                        }
                        unitAdapter.setNewData(resultList)
                    }
                }
                BetUnitDisplayMode.TWO_CHAR.typeNumber -> {
                    it.getView<RecyclerView>(R.id.rvBetUnit).let { recyclerView ->
                        recyclerView.layoutManager = GridLayoutManager(mContext, 5)
                        recyclerView.adapter = unitAdapter
                        var resultList: ArrayList<MultipleBetUnit> = arrayListOf()
                        item?.getData()?.unitList?.forEach { betUnit ->
                            resultList.add(MultipleBetUnit(BetUnitDisplayMode.TWO_CHAR, betUnit))
                        }
                        unitAdapter.setNewData(resultList)
                    }
                }
                BetUnitDisplayMode.THREE_CHAR.typeNumber -> {
                    it.getView<RecyclerView>(R.id.rvBetUnit).let { recyclerView ->
                        recyclerView.layoutManager = GridLayoutManager(mContext, 4)
                        recyclerView.adapter = unitAdapter
                        var resultList: ArrayList<MultipleBetUnit> = arrayListOf()
                        item?.getData()?.unitList?.forEach { betUnit ->
                            resultList.add(MultipleBetUnit(BetUnitDisplayMode.THREE_CHAR, betUnit))
                        }
                        unitAdapter.setNewData(resultList)
                    }
                }
            }
        }

    }
}