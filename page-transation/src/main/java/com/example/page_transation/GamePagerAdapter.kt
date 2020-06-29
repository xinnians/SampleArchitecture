package com.example.page_transation

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.page_all.view.*

class GamePagerAdapter(context: Context, fakeData: MutableList<TransationFragment.FakeUserGame>) : PagerAdapter() {

    var mFakeUserGame: MutableList<TransationFragment.FakeUserGame> ?= null
    var mContext: Context

    init {
        mFakeUserGame = fakeData
        this.mContext = context
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o == view
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page: Item${position}"
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.page_item_game_name, container, false)
        container.addView(view)
        view.tvLockCashTitle.text = mFakeUserGame?.get(0)?.gameName
        view.tvTotalAssetsTitle.text = mFakeUserGame?.get(1)?.gameName
        view.tvWithdrawTitle.text = mFakeUserGame?.get(2)?.gameName
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        Log.d("msg", "destroyItem")
        container.removeView(any as View)
    }

}