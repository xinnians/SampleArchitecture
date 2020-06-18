package com.example.page_bet.bet

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.base.isOdd
import com.example.base.onClick
import com.example.page_bet.R
import com.example.repository.constant.BetUnitDisplayMode
import com.example.repository.model.bet.MultipleBetUnit
import com.example.repository.model.bet.MultipleLotteryEntity
import com.example.repository.model.bet.MultipleLotteryEntity.Companion.FULL_SCREEN

class BetRegionAdapter(data: List<MultipleLotteryEntity>) : BaseMultiItemQuickAdapter<MultipleLotteryEntity, BaseViewHolder>(data) {

    //TODO init BetUnitAdapter for four DisplayMode
    private var onUnitClickListener: OnUnitClickListener? = null

    private var mUnitSelect: String = ""

    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char)
        addItemType(BetUnitDisplayMode.EDIT_AREA.typeNumber, R.layout.item_bet_region_edit_area)
        addItemType(BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber, R.layout.item_bet_region_any_edit_area)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.EDIT_AREA.typeNumber, R.layout.item_bet_region_edit_area_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber, R.layout.item_bet_region_any_edit_area_full)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleLotteryEntity?) {
        helper?.let {
            Log.e("Ian", "[BetRegionAdapter] convert item:${item?.data}")
            //單式處理邏輯
            if (it.itemViewType == BetUnitDisplayMode.EDIT_AREA.typeNumber || it.itemViewType == BetUnitDisplayMode.EDIT_AREA.typeNumber + FULL_SCREEN || it.itemViewType == BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber || it.itemViewType == BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber + FULL_SCREEN) {

                item?.data?.unitList?.get(0)?.unitValue?.let { values ->
                    it.getView<EditText>(R.id.etBetNumber).apply {
                        setText(values)
                        setSelection(values.length)
                        requestFocus()
                    }
                }

                if (it.itemViewType == BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber || it.itemViewType == BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber + FULL_SCREEN) {

                }

                it.getView<EditText>(R.id.etBetNumber).addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        item?.data?.unitList?.get(0)?.unitValue = mUnitSelect + s.toString()
                        onUnitClickListener?.onUnitClick()
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    }

                })

            } else {  //除了單式以外的邏輯判斷
                it.setText(R.id.tvTitle, item?.data?.displayTitle ?: "empty")
                var spanCount: Int = 0
                var displayMode: BetUnitDisplayMode = BetUnitDisplayMode.ONLY_NUMBER
                when (it.itemViewType) {
                    BetUnitDisplayMode.ONLY_NUMBER.typeNumber, FULL_SCREEN + BetUnitDisplayMode.ONLY_NUMBER.typeNumber -> {
                        spanCount = 5
                        displayMode = BetUnitDisplayMode.ONLY_NUMBER

                        it.getView<TextView>(R.id.tvFunctionAll).onClick {
                            item?.data?.let { betData ->
                                betData.unitList.forEach { unitList -> unitList.isSelect = true }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionBig).onClick {
                            item?.data?.let { betData ->
                                val halfSize = betData.unitList.size / 2
                                betData.unitList.forEach { unit ->
                                    unit.isSelect = false
                                    if (unit.unitValue.toInt() >= halfSize) unit.isSelect = true
                                }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionSmall).onClick {
                            item?.data?.let { betData ->
                                val halfSize = betData.unitList.size / 2
                                betData.unitList.forEach { unit ->
                                    unit.isSelect = false
                                    if (unit.unitValue.toInt() < halfSize) unit.isSelect = true
                                }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionOdd).onClick {
                            item?.data?.let { betData ->
                                betData.unitList.forEach { unit ->
                                    unit.isSelect = false
                                    if (unit.unitValue.toInt().isOdd()) unit.isSelect = true
                                }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionEven).onClick {
                            item?.data?.let { betData ->
                                betData.unitList.forEach { unit ->
                                    unit.isSelect = false
                                    if (!unit.unitValue.toInt().isOdd()) unit.isSelect = true
                                }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionClear).onClick {
                            item?.data?.let { betData ->
                                betData.unitList.forEach { unit -> unit.isSelect = false }
                                betData.isDataSet = false
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }


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
                    var unitAdapter: BetUnitAdapter = BetUnitAdapter(listOf(), it.layoutPosition)
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
                            for (item in list) {
                                if (item.isSelect) {
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
    }

    interface OnUnitClickListener {
        fun onUnitClick()
    }

    fun setOnUnitClickListener(listener: OnUnitClickListener) {
        this.onUnitClickListener = listener
    }

    data class UnitSelect(var million: Boolean = false,
                          var thousand: Boolean = false,
                          var hundred: Boolean = false,
                          var ten: Boolean = false,
                          var one: Boolean = false)

    fun UnitSelect.toSelectNumber(): String {
        var recordList: ArrayList<String> = arrayListOf()
        if (million) recordList.add("0")
        if (thousand) recordList.add("1")
        if (hundred) recordList.add("2")
        if (ten) recordList.add("3")
        if (one) recordList.add("4")

        var result: String = ""

        recordList.

        return result
    }

}