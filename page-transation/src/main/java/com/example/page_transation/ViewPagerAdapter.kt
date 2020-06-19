package com.example.page_transation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import kotlinx.android.synthetic.main.page_all.view.*

class ViewPagerAdapter(fakeData: TransationFragment.FakeUserCash) : PagerAdapter() {

    var mFakeData: TransationFragment.FakeUserCash ?= null
    var mFakeDataDetail: MutableList<TransationFragment.FakeUserCashDetail> ?= null
    var mTransAdapter: TransAdapter

    init {
        mFakeData = fakeData
        mFakeDataDetail = fakeData.data
        Log.d("msg", "fakeData size: ${fakeData.data.size}")
        mTransAdapter = TransAdapter(mFakeDataDetail!!)
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return o == view
    }

    override fun getCount(): Int {
        return 7
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Page: Item${position}"
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        Log.d("msg", "instantiateItem position: ${position}")
        when(position) {
            2 -> {
                val view = LayoutInflater.from(container.context).inflate(R.layout.page_cash, container, false)
                container.addView(view)
                return view
            }
            else -> {
                val view = LayoutInflater.from(container.context).inflate(R.layout.page_all, container, false)
                container.addView(view)
                view.tvFliterDate.text = getPageTitle(position)
                view.tvTotalAssets.text = mFakeData?.totalCash.toString()
                view.tvWithdraw.text = mFakeData?.withdraw.toString()
                view.tvLockCash.text = mFakeData?.lockCash.toString()
                var layoutManager = LinearLayoutManager(view.context)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                view.recyclerTrans.layoutManager = layoutManager
                view.recyclerTrans.adapter = mTransAdapter
                return view
            }
        }
    }

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        Log.d("msg", "destroyItem")
        container.removeView(any as View)
    }
}