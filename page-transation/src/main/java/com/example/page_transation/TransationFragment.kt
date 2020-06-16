package com.example.page_transation

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.base.BaseActivity
import com.example.base.BaseFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_transation.*
import me.vponomarenko.injectionmanager.x.XInjectionManager

class TransationFragment : BaseFragment() {

    companion object {
        private val ALL = 0
        private val CASH = 1
        private val BALANCE = 2
        private val TRANSFER = 3
        private val PROXY = 4
        private val BOUNS = 5
        private val ADJUSTMENT = 6
    }

    private var isHide = true
    private lateinit var mTransationViewModel: TransationViewModel

    private val navigation: TransationNavigation by lazy {
        XInjectionManager.findComponent<TransationNavigation>()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_transation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @SuppressLint("ResourceType")
    private fun initView() {
        (activity as BaseActivity?)?.showBottomNav()
        tabTransationType.setSelectedTabIndicator(Color.TRANSPARENT)
        for(index in 0..6) {
            var tab = tabTransationType.getTabAt(index)
            tab?.setCustomView(R.layout.item_transation_tab)
            var holder = ViewHolder(tab?.customView!!)
            holder.imgTabItem?.background = resources.getDrawable(R.drawable.bg_selected_circle)
            holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_balance_account))
            holder.tvTabItem?.setTextColor(Color.parseColor("#7c7e83"))
            when(index) {
                ALL -> {
                    holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_selected_balance_account))
                    holder.tvTabItem?.setTextColor(Color.BLACK)
                    holder.tvTabItem?.text = "全部"
                }
                CASH -> { holder.tvTabItem?.text = "資金" }
                BALANCE -> { holder.tvTabItem?.text = "盈虧" }
                TRANSFER -> { holder.tvTabItem?.text = "轉帳" }
                PROXY -> { holder.tvTabItem?.text = "代理" }
                BOUNS -> { holder.tvTabItem?.text = "彩金" }
                ADJUSTMENT -> { holder.tvTabItem?.text = "調整" }
            }
        }

        tabTransationType.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
                // TODO
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabUnselected")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_balance_account))
                holder.tvTabItem?.setTextColor(Color.parseColor("#7c7e83"))
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                Log.d("msg", "onTabSelected")
                var holder = ViewHolder(tab?.customView!!)
                holder.imgTabItem?.setImageDrawable(resources.getDrawable(R.drawable.ic_selected_balance_account))
                holder.tvTabItem?.setTextColor(Color.BLACK)
            }

        })
    }

    class ViewHolder(view:View) : TabLayout.Tab() {
        var imgTabItem:ImageView?=null
        var tvTabItem:TextView?=null
        init {
            imgTabItem = view.findViewById<ImageView>(R.id.imgTabItem)
            tvTabItem = view.findViewById<TextView>(R.id.tvTabItem)
        }
    }


}