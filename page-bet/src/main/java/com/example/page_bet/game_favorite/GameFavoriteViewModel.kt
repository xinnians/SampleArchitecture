package com.example.page_bet.game_favorite

import androidx.lifecycle.ViewModel
import com.example.repository.model.bet.GameMenuResponse
import com.example.repository.model.bet.MultipleMenuItem

class GameFavoriteViewModel : ViewModel() {
    fun getFavoriteList(list: ArrayList<MultipleMenuItem>?) : MutableList<MultipleMenuItem>{
        var resultList: MutableList<MultipleMenuItem> = mutableListOf()
        var favoriteGames: List<GameMenuResponse.Data.GameInfoEntity> = listOf()

        if (list != null) {
//            for (item in list){
//                if(item.getData()?.id == 7){
//                    favoriteGames = item.getData()?.gameInfoEntityList ?: listOf()
//                    list.remove(item)
//                }
//            }
            var filterList = list.filter {
                if(it.getData()?.id == 7){
                    favoriteGames = it.getData()?.gameInfoEntityList ?: listOf()
                    false
                }else{
                    true
                } }

            for(group in filterList){
                for(game in group.getData()?.gameInfoEntityList?: listOf()){
                    if(favoriteGames.contains(game)){
                        game.isFavorite = true
                    }
                }
                resultList.add(group)
            }
        }

        return resultList
    }
}