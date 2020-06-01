package com.example.repository.model.bet

import com.example.repository.constant.BetUnitDisplayMode

data class BetSelectNumber(var playTypeCode:String = "", var betNumber:String = "")

data class BetData(var displayTitle: String, var unitList: ArrayList<BetUnit>, var isSelect: Boolean = false, var isDataSet: Boolean = false)

data class BetUnit(var unitName: String,var unitValue: Int, var isSelect: Boolean = false, var displayMode: BetUnitDisplayMode)