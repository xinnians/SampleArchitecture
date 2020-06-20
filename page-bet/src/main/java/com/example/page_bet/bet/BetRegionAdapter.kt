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
import com.example.repository.constant.BetItemType
import com.example.repository.constant.BetUnitDisplayMode
import com.example.repository.model.bet.MultipleBetUnit
import com.example.repository.model.bet.MultipleLotteryEntity
import com.example.repository.model.bet.MultipleLotteryEntity.Companion.FULL_SCREEN

class BetRegionAdapter(data: List<MultipleLotteryEntity>) : BaseMultiItemQuickAdapter<MultipleLotteryEntity, BaseViewHolder>(data) {

    //TODO init BetUnitAdapter for four DisplayMode
    private var onUnitClickListener: OnUnitClickListener? = null

    private var mUnitSelect: String = ""
    private var mEditText: String = ""

    init {
        addItemType(BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number)
        addItemType(BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char)
        addItemType(BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char)
        addItemType(BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char)
        addItemType(BetUnitDisplayMode.EDIT_AREA.typeNumber, R.layout.item_bet_region_edit_area)
        addItemType(BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber, R.layout.item_bet_region_any_edit_area)
        addItemType(BetUnitDisplayMode.ANY_ONLY_NUMBER.typeNumber, R.layout.item_bet_region_any_only_number)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ONLY_NUMBER.typeNumber, R.layout.item_bet_region_only_number_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ONE_CHAR.typeNumber, R.layout.item_bet_region_one_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.TWO_CHAR.typeNumber, R.layout.item_bet_region_twp_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.THREE_CHAR.typeNumber, R.layout.item_bet_region_three_char_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.EDIT_AREA.typeNumber, R.layout.item_bet_region_edit_area_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ANY_EDIT_AREA.typeNumber, R.layout.item_bet_region_any_edit_area_full)
        addItemType(FULL_SCREEN + BetUnitDisplayMode.ANY_ONLY_NUMBER.typeNumber, R.layout.item_bet_region_any_only_number_full)
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
                    //TODO init任選單式相關的作動UI
                    item?.data?.unitList?.get(0)?.unitSelect?.million?.apply {
                        it.getView<TextView>(R.id.tvUnitMillion).onClick {
                            item.data.unitList[0].unitSelect.million = this == false
                            notifyDataSetChanged()
                            onItemSelect(item)
                        }
                        it.setBackgroundRes(R.id.tvUnitMillion, if (!this) R.drawable.bg_white_10_corner else R.drawable.bg_gray_10_corner)
                        it.getView<TextView>(R.id.tvUnitMillion).elevation = if (!this) 10f else 0f
                    }

                    item?.data?.unitList?.get(0)?.unitSelect?.thousand?.apply {
                        it.getView<TextView>(R.id.tvUnitThousand).onClick {
                            item.data.unitList[0].unitSelect.thousand = this == false
                            notifyDataSetChanged()
                            onItemSelect(item)
                        }
                        it.setBackgroundRes(R.id.tvUnitThousand, if (!this) R.drawable.bg_white_10_corner else R.drawable.bg_gray_10_corner)
                        it.getView<TextView>(R.id.tvUnitThousand).elevation = if (!this) 10f else 0f
                    }

                    item?.data?.unitList?.get(0)?.unitSelect?.hundred?.apply {
                        it.getView<TextView>(R.id.tvUnitHundred).onClick {
                            item.data.unitList[0].unitSelect.hundred = this == false
                            notifyDataSetChanged()
                            onItemSelect(item)
                        }
                        it.setBackgroundRes(R.id.tvUnitHundred, if (!this) R.drawable.bg_white_10_corner else R.drawable.bg_gray_10_corner)
                        it.getView<TextView>(R.id.tvUnitHundred).elevation = if (!this) 10f else 0f
                    }

                    item?.data?.unitList?.get(0)?.unitSelect?.ten?.apply {
                        it.getView<TextView>(R.id.tvUnitTen).onClick {
                            item.data.unitList[0].unitSelect.ten = this == false
                            notifyDataSetChanged()
                            onItemSelect(item)
                        }
                        it.setBackgroundRes(R.id.tvUnitTen, if (!this) R.drawable.bg_white_10_corner else R.drawable.bg_gray_10_corner)
                        it.getView<TextView>(R.id.tvUnitTen).elevation = if (!this) 10f else 0f
                    }

                    item?.data?.unitList?.get(0)?.unitSelect?.one?.apply {
                        it.getView<TextView>(R.id.tvUnitOne).onClick {
                            item.data.unitList[0].unitSelect.one = this == false
                            notifyDataSetChanged()
                            onItemSelect(item)
                        }
                        it.setBackgroundRes(R.id.tvUnitOne, if (!this) R.drawable.bg_white_10_corner else R.drawable.bg_gray_10_corner)
                        it.getView<TextView>(R.id.tvUnitOne).elevation = if (!this) 10f else 0f
                    }
                }

                it.getView<EditText>(R.id.etBetNumber).addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        mEditText = s.toString()
                        onItemSelect(item)
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
                                for (index in 0 until betData.unitList.size) {
                                    betData.unitList[index].isSelect = index >= halfSize
                                }
                                betData.isDataSet = true
                                onUnitClickListener?.onUnitClick()
                                it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                            }
                        }

                        it.getView<TextView>(R.id.tvFunctionSmall).onClick {
                            item?.data?.let { betData ->
                                val halfSize = betData.unitList.size / 2
                                for (index in 0 until betData.unitList.size) {
                                    betData.unitList[index].isSelect = index < halfSize
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
                    BetUnitDisplayMode.ONE_CHAR.typeNumber, FULL_SCREEN + BetUnitDisplayMode.ONE_CHAR.typeNumber -> {
                        if(item?.data?.betItemType == BetItemType.DRAGON_TIGER_POSITION_BET_TYPE){
                            spanCount = 3
                        }else{
                            spanCount = 2
                        }
                        displayMode = BetUnitDisplayMode.ONE_CHAR
                    }
                    BetUnitDisplayMode.TWO_CHAR.typeNumber, FULL_SCREEN + BetUnitDisplayMode.TWO_CHAR.typeNumber -> {
                        if(item?.data?.betItemType == BetItemType.SPECIAL_BET_TYPE) {
                            spanCount = 3
                        } else {
                            spanCount = 5
                        }
                        displayMode = BetUnitDisplayMode.TWO_CHAR
                    }
                    BetUnitDisplayMode.THREE_CHAR.typeNumber -> {
                        spanCount = 4
                        displayMode = BetUnitDisplayMode.THREE_CHAR
                    }
                }
                if(it.itemViewType != BetUnitDisplayMode.ONLY_NUMBER.typeNumber || it.itemViewType != FULL_SCREEN + BetUnitDisplayMode.ONLY_NUMBER.typeNumber){
                    it.getView<TextView>(R.id.tvFunctionClear).onClick {
                        item?.data?.let { betData ->
                            betData.unitList.forEach { unit -> unit.isSelect = false }
                            betData.isDataSet = false
                            onUnitClickListener?.onUnitClick()
                            it.getView<RecyclerView>(R.id.rvBetUnit).adapter?.notifyDataSetChanged()
                        }
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
                        Log.e("Ian","[mData] size:${mData.size}")
                        mData.forEach { betData -> Log.e("Ian","[mData] ${betData.data}") }
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

    private fun onItemSelect(item: MultipleLotteryEntity?) {
        item?.data?.unitList?.get(0)?.unitValue = mUnitSelect + mEditText
        onUnitClickListener?.onUnitClick()
    }
}