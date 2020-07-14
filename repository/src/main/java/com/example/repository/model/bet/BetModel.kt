package com.example.repository.model.bet

import com.example.repository.constant.BetItemType
import com.example.repository.constant.BetUnitDisplayMode

data class BetSelectNumber(var playTypeCode: String = "", var betNumber: String = "")

data class BetData(var displayTitle: String,
                   var unitList: ArrayList<BetUnit>,
                   var isSelect: Boolean = false,
                   var isDataSet: Boolean = false,
                   var betItemType: BetItemType)

data class BetUnit(var unitName: String,
                   var unitValue: String,
                   var isSelect: Boolean = false,
                   var displayMode: BetUnitDisplayMode,
                   var unitSelect: UnitSelect = UnitSelect())

data class UnitSelect(var million: Boolean = false,
                      var thousand: Boolean = false,
                      var hundred: Boolean = false,
                      var ten: Boolean = false,
                      var one: Boolean = false)

fun UnitSelect.toSelectNumber(): String {
    var recordList: StringBuilder = StringBuilder()
    var isFirstItemSelect: Boolean = false

    if (million) {
        if (!isFirstItemSelect) isFirstItemSelect = true
        recordList.append("0")
    }
    if (thousand) {
        if (!isFirstItemSelect) isFirstItemSelect = true else recordList.append(",")
        recordList.append("1")
    }
    if (hundred) {
        if (!isFirstItemSelect) isFirstItemSelect = true else recordList.append(",")
        recordList.append("2")
    }
    if (ten) {
        if (!isFirstItemSelect) isFirstItemSelect = true else recordList.append(",")
        recordList.append("3")
    }
    if (one) {
        if (!isFirstItemSelect) isFirstItemSelect = true else recordList.append(",")
        recordList.append("4")
    }
    return recordList.toString()
}