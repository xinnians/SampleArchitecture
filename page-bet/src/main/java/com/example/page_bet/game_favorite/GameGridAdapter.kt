package com.example.page_bet.game_favorite

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.page_bet.R
import com.example.repository.model.bet.GameMenuResponse

class GameGridAdapter(data: List<GameMenuResponse.Data.GameInfoEntity>) :
    BaseQuickAdapter<GameMenuResponse.Data.GameInfoEntity, BaseViewHolder>(R.layout.item_grid_game, data) {

    override fun convert(helper: BaseViewHolder?, item: GameMenuResponse.Data.GameInfoEntity?) {
        helper?.setText(R.id.tvGameName,item?.gameName?:"empty")
            ?.addOnClickListener(R.id.tvGameName)
        Log.e("Ian","[GameGridAdapter] item:$item")
        if(item?.isFavorite == true){
            helper?.setBackgroundRes(R.id.layoutItem,R.drawable.bg_favorite_item_saved)
                ?.setTextColor(R.id.tvGameName,mContext.getColor(R.color.colorWhite))
        }else{
            helper?.setBackgroundRes(R.id.layoutItem,if(item?.isClick == true) R.drawable.bg_favorite_item_clicked else R.drawable.bg_favorite_item_normal)
                ?.setTextColor(R.id.tvGameName,mContext.getColor(R.color.colorLittleBlack))
        }
    }
}