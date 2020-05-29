package com.example.page_bet.bet

import android.annotation.SuppressLint
import android.content.Context
import com.example.page_bet.R
import com.example.repository.constant.*
import com.example.repository.model.bet.BetData
import com.example.repository.model.bet.BetUnit
import com.example.repository.model.bet.BetUnitDisplayMode

object BetItemUtil {

    private var hashMapForZeroToNine = hashMapOf(
        "0" to false, "1" to false, "2" to false, "3" to false, "4" to false,
        "5" to false, "6" to false, "7" to false, "8" to false, "9" to false)

    private var templateForZeroToNine = arrayListOf(
        BetUnit("0",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("1",1,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("2",2,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("3",3,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("4",4,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("5",5,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("6",6,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("7",7,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("8",8,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("9",9,false,BetUnitDisplayMode.ONLY_NUMBER))

    private var hashMapForZeroToTwentyseven = hashMapOf(
        "0" to false, "1" to false, "2" to false, "3" to false, "4" to false,
        "5" to false, "6" to false, "7" to false, "8" to false, "9" to false,
        "10" to false, "11" to false, "12" to false, "13" to false, "14" to false,
        "15" to false, "16" to false, "17" to false, "18" to false, "19" to false,
        "20" to false, "21" to false, "22" to false, "23" to false, "24" to false,
        "25" to false, "26" to false, "27" to false)

    private var templateForZeroToTwentyseven = arrayListOf(
        BetUnit("0",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("1",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("2",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("3",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("4",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("5",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("6",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("7",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("8",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("9",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("10",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("11",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("12",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("13",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("14",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("15",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("16",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("17",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("18",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("19",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("20",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("21",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("22",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("23",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("24",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("25",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("26",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("27",0,false,BetUnitDisplayMode.ONLY_NUMBER))

    private var hashMapForOneToTwentySix = hashMapOf(
        "1" to false, "2" to false, "3" to false, "4" to false,
        "5" to false, "6" to false, "7" to false, "8" to false, "9" to false,
        "10" to false, "11" to false, "12" to false, "13" to false, "14" to false,
        "15" to false, "16" to false, "17" to false, "18" to false, "19" to false,
        "20" to false, "21" to false, "22" to false, "23" to false, "24" to false,
        "25" to false, "26" to false)

    private var templateForZeroToTwentySix = arrayListOf(
        BetUnit("0",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("1",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("2",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("3",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("4",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("5",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("6",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("7",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("8",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("9",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("10",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("11",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("12",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("13",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("14",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("15",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("16",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("17",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("18",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("19",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("20",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("21",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("22",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("23",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("24",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("25",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("26",0,false,BetUnitDisplayMode.ONLY_NUMBER),
        BetUnit("27",0,false,BetUnitDisplayMode.ONLY_NUMBER))

    private var hashMapForOneTwoThree = hashMapOf(
        "1" to false, "2" to false, "3" to false)

    private var hashMapForZeroToEighteen = hashMapOf(
        "0" to false, "1" to false, "2" to false, "3" to false, "4" to false,
        "5" to false, "6" to false, "7" to false, "8" to false, "9" to false,
        "10" to false, "11" to false, "12" to false, "13" to false, "14" to false,
        "15" to false, "16" to false, "17" to false, "18" to false)

    private var hashMapForOneToSeventeen = hashMapOf(
        "1" to false, "2" to false, "3" to false, "4" to false,
        "5" to false, "6" to false, "7" to false, "8" to false, "9" to false,
        "10" to false, "11" to false, "12" to false, "13" to false, "14" to false,
        "15" to false, "16" to false, "17" to false)

    private var hashMapForOneToFour = hashMapOf(
        "1" to false, "2" to false, "3" to false, "4" to false)

    @Suppress("UNCHECKED_CAST")
    @SuppressLint("ResourceType")
    fun getTypeData(context: Context, typeCode: String): Triple<BetItemType, List<BetData>, BetItemType> {
        var itemType: BetItemType = BetItemType.NONE
        var itemSecondType: BetItemType = BetItemType.NONE
        var stringArray: List<String> = listOf()
        var betArray: ArrayList<BetData> = arrayListOf()

//        itemType = BetItemType.DEFAULT_BET_TYPE
//        stringArray = ctx.resources.getStringArray(R.array.fiveStar).toList()
//
//
//
//        for (text in stringArray) {
//            betArray.add(BetData(displayTitle = text, unitMap = hashMapForZeroToNine.clone() as HashMap<String, Boolean>))
//        }

        when (typeCode) {
            //---------------------------------------------時時彩-獎金盤---------------------------------------------
            //五星-直選複式
            //五星-直選組合
            //定位膽-五星定位膽
            //任選二-直選複式
            //任選三-直選複式
            //任選四-直選複式
            playTypeID_205000, playTypeID_205010, playTypeID_205040, playTypeID_202000, playTypeID_203000, playTypeID_204000 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar).toList()


//                for (text in stringArray){
//                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForZeroToNine.clone() as HashMap<String, Boolean>))
//                }
            }
            //五星-直選單式
            playTypeID_205001 -> {

            }
            //五星-組選120
            playTypeID_205021 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set120).toList()

            }
            //五星-組選60
            playTypeID_205022 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set60).toList()
            }
            //五星-組選30
            playTypeID_205023 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set30).toList()
            }
            //五星-組選20
            playTypeID_205024 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set20).toList()
            }
            //五星-組選10
            playTypeID_205025 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set10).toList()
            }
            //五星-組選5
            playTypeID_205026 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_set5).toList()
            }
            //五星-一帆風順
            playTypeID_205051 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_one).toList()
            }
            //五星-好事成雙
            playTypeID_205052 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_two).toList()
            }
            //五星-三星報喜
            playTypeID_205053 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_three).toList()
            }
            //五星-四季發財
            playTypeID_205054 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fiveStar_four).toList()
            }

            //---------------------------------------------四星---------------------------------------------
            //四星-前四複式
            //四星-前四組合
            playTypeID_204100, playTypeID_204110 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_front).toList()
            }
            //四星-前四組選24
            //四星-後四組選24
            playTypeID_204121, playTypeID_204221 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_set24).toList()
            }
            //四星-前四組選12
            playTypeID_204122, playTypeID_204222 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_set12).toList()
            }
            //四星-前四組選6
            //四星-後四組選6
            playTypeID_204123, playTypeID_204223 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_set6).toList()
            }
            //四星-前四組選4
            //四星-後四組選4
            playTypeID_204124, playTypeID_204224 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_set4).toList()
            }
            //四星-後四複式
            //四星-後四組合
            playTypeID_204200, playTypeID_204210 ->{
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.fourStar_last).toList()
            }
            //四星-前四單式
            playTypeID_204101 ->{

            }
            //四星-後四單式
            playTypeID_204201 ->{

            }


            //---------------------------------------------三星---------------------------------------------
            //前三-直選複式
            //前三-直選組合
            playTypeID_203100, playTypeID_203110 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.frontThree).toList()
            }

            //前三-直選單式
            playTypeID_203101 -> {

            }

            //前三-直选和值
            //中三-直选和值
            //後三-直选和值
            playTypeID_203102, playTypeID_203202, playTypeID_203302 -> {
                itemType = BetItemType.SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }

            //前三-直選跨度
            //中三-直選跨度
            //後三-直選跨度
            playTypeID_203103, playTypeID_203203, playTypeID_203303 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSpan).toList()
            }

            //前三-組三復式
            //中三-組三復式
            //後三-組三復式
            playTypeID_203123, playTypeID_203223, playTypeID_203323 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.threeSet3).toList()
            }
            //前三-組三單式
            playTypeID_203121 -> {

            }
            //前三-組六複式
            //中三-組六複式
            //後三-組六複式
            playTypeID_203124, playTypeID_203224, playTypeID_203324 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.threeSet6).toList()
            }
            //前三-組六單式
            playTypeID_203122 -> {

            }
            //前三-組選混合
            playTypeID_203130 -> {

            }
            //前三-組選合值
            //中三-組選合值
            //後三-組選合值
            playTypeID_203131, playTypeID_203231, playTypeID_203331 -> {
                itemType = BetItemType.SET_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //前三-組選包膽
            //中三-組選包膽
            //後三-組選包膽
            playTypeID_203133, playTypeID_203233, playTypeID_203333 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betBaodan).toList()
            }
            //前三-和值尾数
            //中三-和值尾数
            //後三-和值尾数
            playTypeID_203134, playTypeID_203234, playTypeID_203334 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.threeLastSum).toList()
            }
            //前三-特殊号码(豹子,顺子, 对子)
            //中三-特殊号码(豹子,顺子, 对子)
            //後三-特殊号码(豹子,顺子, 对子)
            playTypeID_203135, playTypeID_203235, playTypeID_203335 -> {
                itemType = BetItemType.SPECIAL_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.threeSpecial).toList()
            }

            //中三-直選複式
            //中三-直選組合
            playTypeID_203200, playTypeID_203210 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.middleThree).toList()
            }

            //後三-直選複式
            //後三-直選組合
            playTypeID_203300, playTypeID_203310 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.lastThree).toList()
            }

            //---------------------------------------------前二、後二---------------------------------------------
            //前二-直選複式
            playTypeID_202100 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.frontTwo).toList()
            }

            //後二-直選複式
            playTypeID_202200 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.lastTwo).toList()
            }
            //前二-直選單式
            //後二-直選單式
            playTypeID_202101, playTypeID_202201 -> {

            }
            //前二-直選和值
            //後二-直選和值
            playTypeID_202102, playTypeID_202202 -> {
                itemType = BetItemType.TWO_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //前二-直選跨度
            //後二-直選跨度
            playTypeID_202103, playTypeID_202203 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSpan).toList()
            }
            //前二-組選複式
            //後二-組選複式
            playTypeID_202110, playTypeID_202210 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSet).toList()
            }
            //前二-組選單式
            //後二-組選單式
            playTypeID_202120, playTypeID_202220 -> {

            }
            //前二-組選和值
            //後二-組選和值
            playTypeID_202131, playTypeID_202231 -> {
                itemType = BetItemType.TWO_SET_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //前二-組選包膽
            //後二-組選包膽
            playTypeID_202133, playTypeID_202233 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betBaodan).toList()
            }

            //---------------------------------------------不定位---------------------------------------------
            //不定位-前三一碼
            playTypeID_203140,
                //不定位-中三一碼
            playTypeID_203240,
                //不定位-後三一碼
            playTypeID_203340,
                //不定位-前三二碼
            playTypeID_203142,
                //不定位-中三二碼
            playTypeID_203242,
                //不定位-後三二碼
            playTypeID_203342,
                //不定位-前四一碼
            playTypeID_204141,
                //不定位-前四二碼
            playTypeID_204142,
                //不定位-後四一碼
            playTypeID_204241,
                //不定位-後四二碼
            playTypeID_204242,
                //不定位-二碼不定
            playTypeID_205042,
                //不定位-三碼不定
            playTypeID_205043 -> {
                itemType = BetItemType.DEFAULT_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.notPositioned).toList()
            }

            //---------------------------------------------大小單雙---------------------------------------------
            //大小單雙-前二
            playTypeID_202150 -> {
                itemType = BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.frontTwo).toList()
            }
            //大小單雙-後二
            playTypeID_202250 -> {
                itemType = BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.lastTwo).toList()
            }
            //大小單雙-前三
            playTypeID_203150 -> {
                itemType = BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.lastThree).toList()
            }
            //大小單雙-中三
            playTypeID_203250 -> {
                itemType = BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.middleThree).toList()
            }
            //大小單雙-後三
            playTypeID_203350 -> {
                itemType = BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.lastThree).toList()
            }


            //---------------------------------------------龍虎和---------------------------------------------
            //龍虎和
            playTypeID_206010 -> {
                itemType = BetItemType.DRAGON_TIGER_POSITION_BET_TYPE
                itemSecondType = BetItemType.DRAGON_TIGER_NUMBER_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.dragonTigerSum).toList()
            }


            //---------------------------------------------任選2、3、4---------------------------------------------
            //任選二-直選和值
            playTypeID_202002 -> {
                itemType = BetItemType.ANY_TWO_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //任選二-組選複式
            playTypeID_202010 -> {
                itemType = BetItemType.ANY_TWO_SET_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSet).toList()
            }
            //任選二-組選和值
            playTypeID_202031 -> {
                itemType = BetItemType.ANY_TWO_SET_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //任選三-直選和值
            playTypeID_203002 -> {
                itemType = BetItemType.ANY_THREE_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //任選三-組選和值
            playTypeID_203031 -> {
                itemType = BetItemType.ANY_THREE_SET_SUM_BET_TYPE
                stringArray = context.resources.getStringArray(R.array.betSum).toList()
            }
            //任選三-組三複式
            playTypeID_203023 -> {
                itemType = BetItemType.ANY_THREE_SET
                stringArray = context.resources.getStringArray(R.array.threeSet3).toList()
            }
            //任選三-組六複式
            playTypeID_203024 -> {
                itemType = BetItemType.ANY_THREE_SET
                stringArray = context.resources.getStringArray(R.array.threeSet6).toList()
            }
            //任選四-直選單式
            playTypeID_204001 -> {

            }
            //任選四-組選24
            playTypeID_204021 -> {
                itemType = BetItemType.ANY_FOUR_SET
                stringArray = context.resources.getStringArray(R.array.fourStar_set24).toList()
            }
            //任選四-組選12
            playTypeID_204022 -> {
                itemType = BetItemType.ANY_FOUR_SET
                stringArray = context.resources.getStringArray(R.array.fourStar_set12).toList()
            }
            //任選四-組選6
            playTypeID_204023 -> {
                itemType = BetItemType.ANY_FOUR_SET
                stringArray = context.resources.getStringArray(R.array.fourStar_set6).toList()
            }
            //任選四-組選4
            playTypeID_204024 -> {
                itemType = BetItemType.ANY_FOUR_SET
                stringArray = context.resources.getStringArray(R.array.fourStar_set4).toList()
            }
        }

        getBetArray(itemType,stringArray, betArray)

        return Triple(itemType, betArray, itemSecondType)
    }

    private fun getBetArray(betItemType: BetItemType, stringArray: List<String>, betArray: ArrayList<BetData>){

        when(betItemType){
            BetItemType.DEFAULT_BET_TYPE,
            BetItemType.ANY_TWO_SET_BET_TYPE,
            BetItemType.ANY_THREE_SET,
            BetItemType.ANY_FOUR_SET-> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForZeroToNine.clone() as HashMap<String, Boolean>))
                }
            }
            //0-27
            BetItemType.SUM_BET_TYPE,
            BetItemType.ANY_THREE_SUM_BET_TYPE-> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForZeroToTwentyseven.clone() as HashMap<String, Boolean>))
                }
            }
            BetItemType.SET_SUM_BET_TYPE,
            BetItemType.ANY_THREE_SET_SUM_BET_TYPE -> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForOneToTwentySix.clone() as HashMap<String, Boolean>))
                }

            }
            BetItemType.SPECIAL_BET_TYPE -> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForOneTwoThree.clone() as HashMap<String, Boolean>))
                }
            }
            BetItemType.TWO_SUM_BET_TYPE,
            BetItemType.ANY_TWO_SUM_BET_TYPE-> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForZeroToEighteen.clone() as HashMap<String, Boolean>))
                }
            }
            BetItemType.TWO_SET_SUM_BET_TYPE,
            BetItemType.ANY_TWO_SET_SUM_BET_TYPE -> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForOneToSeventeen.clone() as HashMap<String, Boolean>))
                }
            }
            BetItemType.SIZE_SINGLE_DOUBLE_BET_TYPE -> {
                for (text in stringArray){
                    betArray.add(BetData(displayTitle = text,unitMap = hashMapForOneToFour.clone() as HashMap<String, Boolean>))
                }
            }
            BetItemType.DRAGON_TIGER_POSITION_BET_TYPE -> {
                if(stringArray.size == 2){
                    betArray.add(BetData(displayTitle = stringArray[0],unitMap = hashMapForZeroToNine.clone() as HashMap<String, Boolean>))
                    betArray.add(BetData(displayTitle = stringArray[1],unitMap = hashMapForOneTwoThree.clone() as HashMap<String, Boolean>))
                }
            }
        }

    }

    private fun getBetUnitList(start: Int,end: Int){
        for (index in start..end){

        }
    }
}