package com.example.page_bet.bet.lottery_record

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base.onClick
import com.example.page_bet.R
import com.example.repository.model.bet.MultipleHistoryRecord
import kotlinx.android.synthetic.main.dialog_lottery_record.*

class LotteryRecordDialog(var data: MutableList<MultipleHistoryRecord>, context: Context) : Dialog(context) {

    private var mAdapter: HistoryRecordAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_lottery_record)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT
            )
        }
        init()
    }

    private fun init(){
        var layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rvLotteryHistory.layoutManager = layoutManager

        mAdapter = HistoryRecordAdapter(data)
        rvLotteryHistory.adapter = mAdapter

        tvClose.onClick { this.dismiss() }
        tvTrend.onClick { Toast.makeText(context,"未開發",Toast.LENGTH_SHORT).show() }
    }
}