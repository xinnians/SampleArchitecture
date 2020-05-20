package com.example.page_bet.bet_menu

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.MultipleMenuItem

class BetMenuAdapter(data: MutableList<MultipleMenuItem>) :
    BaseMultiItemQuickAdapter<MultipleMenuItem, BaseViewHolder>(data) {
    init {
        addItemType(MultipleMenuItem.HOT, R.layout.item_hot)
        addItemType(MultipleMenuItem.FAVORITE, R.layout.item_favorite)
        addItemType(MultipleMenuItem.NORMAL, R.layout.item_normal)
    }

    override fun convert(helper: BaseViewHolder?, item: MultipleMenuItem?) {
        helper?.let {
            it.setText(R.id.tvTypeName,item?.getData()?.gameTypeDisplayName?:"empty")
            var layoutManager = LinearLayoutManager(mContext)
            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            helper?.getView<RecyclerView>(R.id.rvGameList).layoutManager = layoutManager
            var adapter: GameListAdapter = GameListAdapter(item?.getData()?.gameInfoEntityList?: listOf())
            helper?.getView<RecyclerView>(R.id.rvGameList).adapter = adapter

            when (it.itemViewType) {
                MultipleMenuItem.HOT -> {
                }
                MultipleMenuItem.FAVORITE -> {
                    helper?.addOnClickListener(R.id.ivEdit)
                }
                MultipleMenuItem.NORMAL -> {
                }
                else -> {

                }
            }
        }
    }
}