package com.example.page_bet.game_favorite

import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleMenuItem

class GameFavoriteAdapter(data: MutableList<MultipleMenuItem>) :
    BaseQuickAdapter<MultipleMenuItem, BaseViewHolder>(R.layout.item_game_favorite, data) {

    override fun convert(helper: BaseViewHolder?, item: MultipleMenuItem?) {
        helper?.setText(R.id.tvGameType, item?.getData()?.gameTypeDisplayName ?: "empty")
        var gridLayoutManager = GridLayoutManager(mContext, 3)
        helper?.getView<RecyclerView>(R.id.rvGameList)?.layoutManager = gridLayoutManager
        var gameGridAdapter: GameGridAdapter =
            GameGridAdapter(item?.getData()?.gameInfoEntityList ?: listOf())
        helper?.getView<RecyclerView>(R.id.rvGameList)?.adapter = gameGridAdapter

        if (data.indexOf(item) == data.size - 1)
            helper?.setVisible(R.id.layoutSpace, true)

        gameGridAdapter.setOnItemChildClickListener { adapter, view, position ->
            Log.e("Ian", "[gameGridAdapter] onItemClick $position")
            item?.getData()?.gameInfoEntityList?.get(position)?.let {
                if (it.isFavorite) {
                    it.isFavorite = false
                } else {
                    it.isClick = !it.isClick
                }
                gameGridAdapter.notifyDataSetChanged()
            }
        }
    }
}