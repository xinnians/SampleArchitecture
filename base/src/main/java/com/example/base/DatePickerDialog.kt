package com.example.base

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.WindowManager
import com.savvi.rangedatepicker.CalendarPickerView
import kotlinx.android.synthetic.main.dialog_date_picker.*
import java.text.SimpleDateFormat
import java.util.*

class DatePickerDialog(context: Context, var listener: GetDateListener) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_date_picker)
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        }

        val nextMonth = Calendar.getInstance()
        nextMonth.add(Calendar.MONTH, 0)

        val lastMonth = Calendar.getInstance()
        lastMonth.add(Calendar.DAY_OF_YEAR, -45)

        calendarView.let {
            it.init(lastMonth.time, nextMonth.time, SimpleDateFormat("MMMM, YYYY", Locale.getDefault()))
                .inMode(CalendarPickerView.SelectionMode.RANGE)
            it.scrollToDate(Date())
            it.setOnStartDateClickListener { date ->
                tvStartDay.let {
                    it.text = ""
                    it.text = getDay(date)
                }
                tvStartMonth.let {
                    it.text = ""
                    it.text = getMonth(date)
                }
            }

            it.setOnEndDateClickListener { date ->
                tvEndDay.let {
                    it.text = ""
                    it.text = getDay(date)
                }
                tvEndMonth.let {
                    it.text = ""
                    it.text = getMonth(date)
                }
            }
        }


        ivClose.onClick {
            dismiss()
        }

        btnSearch.onClick {
            val dateList = calendarView.selectedDates
            if (dateList.size == 0) {
                dismiss()
            } else {
                val start = dateList[0]
                val end = dateList[dateList.size - 1]
                val arrayList = arrayListOf(convertDate(start.toString()), convertDate(end.toString()))
                listener.getSearch(arrayList)
                dismiss()
            }
        }
    }

    interface GetDateListener {
        fun getSearch(list: ArrayList<Long>)
    }
}
