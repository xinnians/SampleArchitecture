package com.example.page_bet.bet

import android.util.Log
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultiplePlayTypePositionItem

class BetPositionAdapter(data: List<MultiplePlayTypePositionItem>) : BaseMultiItemQuickAdapter<MultiplePlayTypePositionItem, BaseViewHolder>(data) {

    init {
        //單式不需顯示選項，僅需顯示說明
        addItemType(0, R.layout.item_play_type_position_zero)
        //只有一個選項的時候
        addItemType(1, R.layout.item_play_type_position_one)
        addItemType(2, R.layout.item_play_type_position_two)
        addItemType(3, R.layout.item_play_type_position_three)
        addItemType(4, R.layout.item_play_type_position_four)
        addItemType(5, R.layout.item_play_type_position_five)
    }

    override fun convert(helper: BaseViewHolder?, item: MultiplePlayTypePositionItem?) {
        Log.e("Ian", "[BetPositionAdapter] convert: $item, itemType: ${helper?.itemViewType}")

        helper?.let {
            it.setText(R.id.tvPosition, item?.getData()?.displayTitle)
            it.addOnClickListener(R.id.tvPosition)

            if(it.itemViewType != 0){
                if(item?.getData()?.isSelect == true){
                    it.setTextColor(R.id.tvPosition,mContext.getColor(R.color.colorBlack))
                    it.setBackgroundRes(R.id.tvPosition,R.drawable.bg_white_25_corner_dark_stroke)
                }else{
                    it.setTextColor(R.id.tvPosition,mContext.getColor(R.color.colorLittleGray))
                    if(item?.getData()?.isDataSet == true){
                        it.setBackgroundRes(R.id.tvPosition,R.drawable.bg_darkgray_25_corner)
                    }else{
                        it.setBackgroundRes(R.id.tvPosition,R.drawable.bg_gray_25_corner)
                    }
                }
            }
        }
    }

    fun setFirstItemSelect(){
        data?.let {
            if(it.size>0){
                it[0].getData()?.isSelect = true
            }
        }
        getViewByPosition(0,R.id.tvPosition)?.callOnClick()
        onItemChildClickListener?.onItemChildClick(this,getViewByPosition(0,R.id.tvPosition),0)
    }
}