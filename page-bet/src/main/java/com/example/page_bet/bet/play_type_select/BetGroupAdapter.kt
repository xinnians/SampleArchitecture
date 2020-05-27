package com.example.page_bet.bet.play_type_select

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.BetGroupEntity

class BetGroupAdapter(data: List<BetGroupEntity>) :
    BaseQuickAdapter<BetGroupEntity, BaseViewHolder>(R.layout.item_bet_group, data) {

    private var mOnPlayTypeSelect: OnPlayTypeSelect? = null

    fun setOnPlayTypeSelectListener(listener: OnPlayTypeSelect) {
        mOnPlayTypeSelect = listener
    }

    override fun convert(helper: BaseViewHolder?, item: BetGroupEntity?) {
        helper?.let {
            it.setText(R.id.tvBetGroup, item?.betGroupDisplayName)


            var gridLayoutManager = GridLayoutManager(mContext, 2)
            it.getView<RecyclerView>(R.id.rvPlayType).layoutManager = gridLayoutManager
            var playTypeAdapter =
                PlayTypeAdapter(
                    item?.playTypeInfoEntityList ?: listOf()
                )
            it.getView<RecyclerView>(R.id.rvPlayType).adapter = playTypeAdapter
            playTypeAdapter.setOnItemChildClickListener { adapter, view, position ->
                mOnPlayTypeSelect?.onSelect(
                    item?.playTypeInfoEntityList?.get(position)?.playTypeCode ?: -1,
                    item?.playTypeInfoEntityList?.get(position)?.displayName ?: "empty",
                    item?.betGroupDisplayName ?: "empty"
                )
            }
        }
    }

    interface OnPlayTypeSelect {
        fun onSelect(playTypeCode: Int, playTypeDisplayName: String, betGroupDisplayName: String)
    }
}