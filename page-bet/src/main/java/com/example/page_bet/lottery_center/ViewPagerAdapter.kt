package com.example.page_bet.lottery_center

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleIssueResultItem
import kotlinx.android.synthetic.main.layout_lottery_center_viewpager2_cell.view.*

class ViewPagerAdapter : RecyclerView.Adapter<ViewPagerAdapter.BaseViewHolder>() {

    var mData: MutableList<MutableList<MultipleIssueResultItem>>? = null
    private var clickResultAction: ((result: MultipleIssueResultItem)->Unit) ? =null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_lottery_center_viewpager2_cell, parent, false))

    override fun getItemCount(): Int = mData?.size ?: 0

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        mData?.get(position)?.let {
            holder.bind(it)
        }
        holder.gameInfoAdapter.setOnItemChildClickListener { adapter, view, position ->
            var item: MultipleIssueResultItem = adapter.data.get(position) as MultipleIssueResultItem
            clickResultAction?.invoke(item)
        }
    }

    fun setResultAction(action: (result: MultipleIssueResultItem) -> Unit) {
        this.clickResultAction = action
    }

    class BaseViewHolder(val view: View) : RecyclerView.ViewHolder(view){

        var gameInfoAdapter: GameIssueInfoAdapter = GameIssueInfoAdapter(mutableListOf())

        init {
            var layoutManager: LinearLayoutManager = LinearLayoutManager(view.context)
            layoutManager.orientation = LinearLayoutManager.VERTICAL
            itemView.rvGameIssueInfo.layoutManager = layoutManager
            itemView.rvGameIssueInfo.adapter = gameInfoAdapter
        }

        fun bind(data: MutableList<MultipleIssueResultItem>){
            gameInfoAdapter.setNewData(data)
        }
    }
}