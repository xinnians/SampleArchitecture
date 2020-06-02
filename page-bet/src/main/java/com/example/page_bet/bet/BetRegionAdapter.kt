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
    private var onUnitClickListener: OnUnitClickListener? = null

    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char)
        addItemType(10+BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number_full)
        addItemType(10+BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char_full)
        addItemType(10+BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char_full)
        addItemType(10+BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char_full)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleLotteryEntity?) {
        helper?.let {
            Log.e("Ian", "[BetRegionAdapter] convert item:${item?.data}")
            it.setText(R.id.tvTitle, item?.data?.displayTitle ?: "empty")
            var spanCount: Int = 0
            var displayMode: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER
            when (it.itemViewType) {
                BetUnitDisplayMode.ONLY_NUMBER.typeNumber,
                10+BetUnitDisplayMode.ONLY_NUMBER.typeNumber -> {
                    spanCount = 5
                    displayMode = BetUnitDisplayMode.ONLY_NUMBER
                }
                BetUnitDisplayMode.ONE_CHAR.typeNumber -> {
                    spanCount = 2
                    displayMode = BetUnitDisplayMode.ONE_CHAR
                }
                BetUnitDisplayMode.TWO_CHAR.typeNumber -> {
                    spanCount = 5
                    displayMode = BetUnitDisplayMode.TWO_CHAR
                }
                BetUnitDisplayMode.THREE_CHAR.typeNumber -> {
                    spanCount = 4
                    displayMode = BetUnitDisplayMode.THREE_CHAR
                }
            }
            it.getView<RecyclerView>(R.id.rvBetUnit).let { recyclerView ->
                recyclerView.layoutManager = GridLayoutManager(mContext, spanCount)
                var unitAdapter: BetUnitAdapter = BetUnitAdapter(listOf(),it.adapterPosition)
                recyclerView.adapter = unitAdapter
                var resultList: ArrayList<MultipleBetUnit> = arrayListOf()
                item?.data?.unitList?.forEach { betUnit ->
                    resultList.add(MultipleBetUnit(displayMode, betUnit))
                }
                unitAdapter.setNewData(resultList)
                unitAdapter.setOnItemChildClickListener { adapter, view, position ->
                    mData[unitAdapter.parentPostion].data?.unitList?.get(position)?.isSelect =
                        mData[unitAdapter.parentPostion].data?.unitList?.get(position)?.isSelect == false
                    var isDataSet = false
                    mData[unitAdapter.parentPostion].data?.unitList?.let { list ->
                        for(item in list ){
                            if(item.isSelect){
                                isDataSet = true
                            }
                        }
                    }
                    mData[unitAdapter.parentPostion].data?.isDataSet = isDataSet

                    onUnitClickListener?.onUnitClick()
                    unitAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    interface OnUnitClickListener{
        fun onUnitClick()
    }

    fun setOnUnitClickListener(listener: OnUnitClickListener){
        this.onUnitClickListener = listener
    }

}