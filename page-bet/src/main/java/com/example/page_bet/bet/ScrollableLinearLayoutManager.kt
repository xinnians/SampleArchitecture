package com.example.page_bet.bet

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class ScrollableLinearLayoutManager(context: Context?, var isScrollEnabled: Boolean) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return super.canScrollVertically() && isScrollEnabled
    }
}