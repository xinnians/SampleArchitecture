package com.example.page_bet.bet

import android.util.Log
import com.example.base.isNumeric
import com.example.repository.constant.*
import com.example.repository.model.bet.BetSelectNumber
import com.example.repository.model.bet.MultiplePlayTypePositionItem
import java.net.URLDecoder
import java.util.regex.Pattern

object BetCountUtil {

    fun getBetSelectNumber(playtypeCode: Int, betEntityList: List<MultiplePlayTypePositionItem>): BetSelectNumber {
        var result: StringBuilder = StringBuilder()

        when(playtypeCode.toString()){
            playTypeID_205000,
            playTypeID_205010,
            playTypeID_205022,
            playTypeID_205023,
            playTypeID_205024,
            playTypeID_205025,
            playTypeID_205026,
            playTypeID_204100,
            playTypeID_204110,
            playTypeID_204200,
            playTypeID_204210,
            playTypeID_204122,
            playTypeID_204124,
            playTypeID_204222,
            playTypeID_204224,
            playTypeID_203100,
            playTypeID_203110,
            playTypeID_203103,
            playTypeID_203200,
            playTypeID_203210,
            playTypeID_203300,
            playTypeID_203310,
            playTypeID_202100,
            playTypeID_202200,
            playTypeID_205040,
            playTypeID_202150,
            playTypeID_202250,
            playTypeID_203150,
            playTypeID_203250,
            playTypeID_203350,
            playTypeID_202000
            -> {
                for (index in betEntityList.indices){
                    for (entity in betEntityList[index].getData()?.unitList!!){
                        if(entity.isSelect){
                            result.append(entity.unitValue)
                        }
                    }
                    if(betEntityList.lastIndex != index){
                        result.append(",")
                    }
                }
            }
            playTypeID_205021,
            playTypeID_205051,
            playTypeID_205052,
            playTypeID_205053,
            playTypeID_205054,
            playTypeID_204121,
            playTypeID_204123,
            playTypeID_204221,
            playTypeID_204223,
            playTypeID_203123,
            playTypeID_203124,
            playTypeID_203223,
            playTypeID_203224,
            playTypeID_203234,
            playTypeID_203323,
            playTypeID_203324,
            playTypeID_203334,
            playTypeID_202110,
            playTypeID_202210,
            playTypeID_203140,
            playTypeID_203240,
            playTypeID_203340,
            playTypeID_203142,
            playTypeID_203242,
            playTypeID_203342,
            playTypeID_204141,
            playTypeID_204142,
            playTypeID_204241,
            playTypeID_204242,
            playTypeID_205042,
            playTypeID_205043
            -> {
                for (index in betEntityList.indices){
                    for (entity in betEntityList[index].getData()?.unitList!!){
                        if(entity.isSelect){
                            result.append(entity.unitValue)
                        }
                    }
                }
            }
            playTypeID_203102,
            playTypeID_203131,
            playTypeID_203133,
            playTypeID_203202,
            playTypeID_203231,
            playTypeID_203233,
            playTypeID_203302,
            playTypeID_203331,
            playTypeID_203333,
            playTypeID_202102,
            playTypeID_202131,
            playTypeID_202133,
            playTypeID_202202,
            playTypeID_202231,
            playTypeID_202233
            -> {
                for (index in betEntityList.indices){
                    for (entity in betEntityList[index].getData()?.unitList!!){
                        if(result.isNotEmpty()){
                            result.append("$")
                        }
                        if(entity.isSelect){
                            result.append(entity.unitValue)
                        }
                    }
                }
            }
            playTypeID_203135,
            playTypeID_203203,
            playTypeID_203235,
            playTypeID_203303,
            playTypeID_203335,
            playTypeID_202103,
            playTypeID_202203-> {
                for (index in betEntityList.indices){
                    for (entity in betEntityList[index].getData()?.unitList!!){
                        if(result.isNotEmpty()){
                            result.append(",")
                        }
                        if(entity.isSelect){
                            result.append(entity.unitValue)
                        }
                    }
                }
            }
            playTypeID_206010-> {
                for (index in betEntityList.indices){
                    for (entity in betEntityList[index].getData()?.unitList!!){
                        if(result.isNotEmpty()){
                            result.append(",")
                        }
                        if(entity.isSelect){
                            result.append(entity.unitValue)
                        }
                    }
                    if(betEntityList.lastIndex != index){
                        result.append("@")
                    }
                }
            }
            playTypeID_205001-> {
                result = getSingleBetTypeSelectNumber(5,betEntityList)
            }
            playTypeID_204101,playTypeID_204201-> {
                result = getSingleBetTypeSelectNumber(4,betEntityList)
            }
            playTypeID_203101, playTypeID_203121, playTypeID_203122,
            playTypeID_203201, playTypeID_203221, playTypeID_203222,
            playTypeID_203301, playTypeID_203321, playTypeID_203322-> {
                result = getSingleBetTypeSelectNumber(3,betEntityList)
            }
            playTypeID_202101, playTypeID_202120, playTypeID_202201, playTypeID_202220-> {
                result = getSingleBetTypeSelectNumber(2,betEntityList)
            }
        }
        Log.e("Ian","[getBetSelectNumber] result: $result")
        return BetSelectNumber(playtypeCode.toString(),result.toString())
    }

    private fun getSingleBetTypeSelectNumber(length: Int, betEntityList: List<MultiplePlayTypePositionItem>): StringBuilder{
        var result: StringBuilder = StringBuilder()
        for (index in betEntityList.indices) {
            for (entity in betEntityList[index].getData()?.unitList!!) {
                result.append(entity.unitValue)
            }
            Log.e("Ian","[getSingleBetTypeSelectNumber] oriString: $result")
            var spiltList = result.split(Pattern.compile("[,. ;]"))
            spiltList = spiltList.filter { it.isNumeric() && it.length == length}
            result.clear()
            for(index in spiltList.indices){
                result.append(spiltList[index])
                if(index != spiltList.size-1){
                    result.append(",")
                }
            }
        }
        return result
    }

    /**
     * 會先以傳入的正則表示式檢查該betSelectNumber有無符合正則規則，不符合則回傳0，符合則進行注數運算。
     */
    fun getBetCount(betSelectNumber: BetSelectNumber, regex: String): Int {

        var currentRegex = URLDecoder.decode(regex,"UTF-8")
        Log.e("Ian","[getBetCount] currentRegex:$currentRegex")
        var pattern: Pattern? = null

        if(currentRegex.isNotEmpty()){
            pattern = Pattern.compile(currentRegex)
            pattern?.let {
                if(it.matcher(betSelectNumber.betNumber).matches()){

                }else{
                    return 0
                }
            }
        }else{
            return 0
        }

        val gameID = betSelectNumber.playTypeCode.substring(0, 1)
        val playTypeID = betSelectNumber.playTypeCode.substring(2, 3)

        val currentPlayTypeIndex: String = betSelectNumber.playTypeCode.substring(0, 3)

        return when (gameID) {
            //PK10
            "1" -> getPK10BetCount(betSelectNumber)
            //時時彩
            "2" -> when (playTypeID) {
                "6" -> getMinuteLotteryOtherBetCount(betSelectNumber)
                "5" -> getMinuteLotteryFiveStarBetCount(betSelectNumber)
                "4" -> getMinuteLotteryFourStarBetCount(betSelectNumber)
                "3" -> getMinuteLotteryThreeStarBetCount(betSelectNumber)
                "2" -> getMinuteLotteryTwoStarBetCount(betSelectNumber)
                else -> 0
            }
            //11選5
            "3" -> getElevenChooseFiveBetCount(betSelectNumber)
            //快3
            "4" -> getHurry3BetCount(betSelectNumber)
            else -> 0
        }

    }
    //--------------------------------------------------快3------------------------------------------------------------//

    /**
     * 快3-投注數計算
     */
    private fun getHurry3BetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //二同號標準
            //公式 N1*N2
            //N1: 二同号个数
            //N2: 不同号个数
            playTypeID_402004 -> {
                var resultCount = 1
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                betItemList.forEach { resultCount *= it.split("$").count() }

                resultCount
            }
            //二同號單式,二同號複式,兩不同號單式,三同號標準,三不同號單式,大小單雙,和值
            playTypeID_402003,
            playTypeID_402005,
            playTypeID_402002,
            playTypeID_403003,
            playTypeID_403002,
            playTypeID_407000,
            playTypeID_408000 -> {
                betSelectNumber.betNumber.split(",").count()
            }
            //兩不同號標準
            playTypeID_402001 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            //三不同號標準
            playTypeID_403001 -> {
                functionCombination(betSelectNumber.betNumber.length, 3)
            }
            else -> 0
        }
    }

    //-------------------------------------------------11選5-----------------------------------------------------------//

    /**
     * 11選5-投注數計算
     */
    private fun getElevenChooseFiveBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //前三複式
            //公式 N1*N2*N3 - (n12*N3 + n23*N1 + n13*N2 - 2*n123)
            //N1：第一位选号
            //N2：第二位选号
            //N3：第三位选号
            //n12 : 一二位重複選號
            //n23 : 二三位重複選號
            //n13 : 一三位重複選號
            //n123 : 一二三位重複選號
            playTypeID_303001 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val N1 = betItemList[0].split("$")
                val N2 = betItemList[1].split("$")
                val N3 = betItemList[2].split("$")
                val n12 = N1.count { N2.contains(it) }
                val n23 = N2.count { N3.contains(it) }
                val n13 = N1.count { N3.contains(it) }
                val n123 = N1.count { N2.contains(it) && N3.contains(it) }

                N1.size * N2.size * N3.size - (n12 * N3.size + n23 * N1.size + n13 * N2.size - 2*n123)
            }
            //前二複式 07$08$09$10$11,01$02$03$04$05$06$07$08$09$10$11
            //公式：n1 * n2 - n12
            //n1 : 第一位投注選擇
            //n2 : 第二位投注選擇
            //n12 : 第一位和第二位重複位數
            playTypeID_302001 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val n1 = betItemList[0].split("$")
                val n2 = betItemList[1].split("$")
                val n12 = n1.count { n2.contains(it) }

                n1.count() * n2.count() - n12
            }
            //前三組選複式 example:01$02$03$04$05$06$07
            //任選3中3複式
            //公式 C(N,3)
            playTypeID_303003,
            playTypeID_305005 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 3)
            }
            //前二組選複式
            //任選2中2複式
            //公式 C(N,2)
            playTypeID_302003,
            playTypeID_305003 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 2)
            }
            //任選4中4複式
            playTypeID_305007 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 4)
            }
            //任選5中5複式
            playTypeID_305009 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 5)
            }
            //任選6中5複式
            playTypeID_305011 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 6)
            }
            //任選7中5複式
            playTypeID_305013 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 7)
            }
            //任選8中5複式
            playTypeID_305015 -> {
                functionCombination(betSelectNumber.betNumber.split("$").count(), 8)
            }
            //不定位膽
            //定單雙
            //猜中數
            //任選1中1複式
            playTypeID_303005,
            playTypeID_307000,
            playTypeID_308000,
            playTypeID_305001 -> {
                betSelectNumber.betNumber.split("$").count()
            }
            //定位膽
            playTypeID_303000 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                betItemList.forEach { resultCount += it.split("$").count() }

                resultCount
            }
            //前三單式
            //前三組選單式
            //前二單式
            //前二組選單式
            //任選1中1單式,任選2中2單式,任選3中3單式,任選4中4單式,任選5中5單式,任選6中5單式,任選7中5單式,任選8中5單式
            playTypeID_303002,
            playTypeID_303004,
            playTypeID_302002,
            playTypeID_302004,
            playTypeID_305002,
            playTypeID_305004,
            playTypeID_305006,
            playTypeID_305008,
            playTypeID_305010,
            playTypeID_305012,
            playTypeID_305014,
            playTypeID_305016 -> {
                betSelectNumber.betNumber.split(",").count()
            }
            else -> 0
        }
    }

    //-------------------------------------------------PK10------------------------------------------------------------//

    /**
     * PK10-投注數計算
     */
    private fun getPK10BetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //定位膽 公式 N1+N2+N3+N4+N5+N6+N7+N8+N9+N10
            playTypeID_105000 -> {
                var resultCount = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += item.split("$").size
                }

                resultCount
            }
            //猜前二複式
            //公式 N1*N2 - n
            //N1：冠军选号
            //N2：亚军选号
            //n: 冠军与亚军中重复的选号
            playTypeID_102002 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val number1List = betItemList[0].split("$")
                val number2List = betItemList[1].split("$")

                number1List.size * number2List.size - number1List.isSameCount(number2List)

            }
            //猜前三複式
            //公式 N1*N2*N3 - (n12*N3 + n23*N1 + n13*N2 - 2*n123)
            //N1：冠军选号
            //N2：亚军选号
            //N3：季军选号
            //n12 : 冠亞重複選號
            //n23 : 亞季重複選號
            //n13 : 冠季重複選號
            //n123 : 冠亞季重複選號
            playTypeID_102003 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val N1 = betItemList[0].split("$")
                val N2 = betItemList[1].split("$")
                val N3 = betItemList[2].split("$")
                val n12 = N1.count { N2.contains(it) }
                val n23 = N2.count { N3.contains(it) }
                val n13 = N1.count { N3.contains(it) }
                val n123 = N1.count { N2.contains(it) && N3.contains(it) }

                N1.size * N2.size * N3.size - (n12 * N3.size + n23 * N1.size + n13 * N2.size - 2*n123)
            }
            //猜前四複式
            //公式 N1*N2*N3*N4 - 不符合PK10规则的注数
            //N1：冠军选号
            //N2：亚军选号
            //N3：季军选号
            //N4：第4名选号
            playTypeID_102004 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val N1 = betItemList[0].split("$")
                val N2 = betItemList[1].split("$")
                val N3 = betItemList[2].split("$")
                val N4 = betItemList[3].split("$")

                //TODO 缺少扣除不符合PK10规则注数的計算

                N1.size * N2.size * N3.size * N4.size
            }
            //猜前五複式
            //公式 N1*N2*N3*N4*N5 - 不符合PK10规则的注数
            //N1：冠军选号
            //N2：亚军选号
            //N3：季军选号
            //N4：第4名选号
            //N5：第5名选号
            playTypeID_102005 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val N1 = betItemList[0].split("$")
                val N2 = betItemList[1].split("$")
                val N3 = betItemList[2].split("$")
                val N4 = betItemList[3].split("$")
                val N5 = betItemList[4].split("$")

                //TODO 缺少扣除不符合PK10规则注数的計算

                N1.size * N2.size * N3.size * N4.size * N5.size
            }
            //猜前二單式、猜前三單式、猜前四單式、猜前五單式
            playTypeID_104002,
            playTypeID_104003,
            playTypeID_104004,
            playTypeID_104005 -> {
                betSelectNumber.betNumber.split(",").size
            }
            //冠亞和值
            //冠亞大小單雙
            playTypeID_108000,
            playTypeID_107101 -> {
                betSelectNumber.betNumber.split("$").size
            }
            //按名次大小單雙
            //龍虎
            playTypeID_107006,
            playTypeID_106006 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                betItemList.forEach { resultCount += it.split("$").count() }

                resultCount
            }
            else -> {
                0
            }
        }
    }

    //------------------------------------------------時時彩------------------------------------------------------------//

    // 三星直選和值投注數對照表
    private val threeStarDirectSelectSumKeymap = mapOf(
        "0" to 1, "1" to 3, "2" to 6, "3" to 10, "4" to 15, "5" to 21,
        "6" to 28, "7" to 36, "8" to 45, "9" to 55, "10" to 63, "11" to 69,
        "12" to 73, "13" to 75, "14" to 75, "15" to 73, "16" to 69, "17" to 63,
        "18" to 55, "19" to 45, "20" to 36, "21" to 28, "22" to 21, "23" to 15,
        "24" to 10, "25" to 6, "26" to 3, "27" to 1
    )

    // 三星直選跨度投注數對照表
    private val threeStarDirectSelectSpanKeymap = mapOf(
        "0" to 10, "1" to 54, "2" to 96, "3" to 126, "4" to 144,
        "5" to 150, "6" to 144, "7" to 126, "8" to 96, "9" to 54
    )

    // 三星組選和值投注數對照表
    private val threeStarCombineSelectSumKeymap = mapOf(
        "1" to 1, "2" to 2, "3" to 2, "4" to 4, "5" to 5,
        "6" to 6, "7" to 8, "8" to 10, "9" to 11, "10" to 13,
        "11" to 14, "12" to 14, "13" to 15, "14" to 15, "15" to 14,
        "16" to 14, "17" to 13, "18" to 11, "19" to 10, "20" to 8,
        "21" to 6, "22" to 5, "23" to 4, "24" to 2, "25" to 2, "26" to 1
    )

    // 二星直選和值投注數對照表
    private val twoStarDirectSelectSumKeymap = mapOf(
        "0" to 1, "1" to 2, "2" to 3, "3" to 4, "4" to 5,
        "5" to 6, "6" to 7, "7" to 8, "8" to 9, "9" to 10,
        "10" to 9, "11" to 8, "12" to 7, "13" to 6, "14" to 5,
        "15" to 4, "16" to 3, "17" to 2, "18" to 1
    )

    // 二星直選跨度投注數對照表
    private val twoStarDirectSelectSpanKeymap = mapOf(
        "0" to 10, "1" to 18, "2" to 16, "3" to 14, "4" to 12,
        "5" to 10, "6" to 8, "7" to 6, "8" to 4, "9" to 2
    )

    // 二星組選和值投注數對照表
    private val twoStarCombineSelectSumKeymap = mapOf(
        "1" to 1, "2" to 1, "3" to 2, "4" to 2, "5" to 3,
        "6" to 3, "7" to 4, "8" to 4, "9" to 5, "10" to 4,
        "11" to 4, "12" to 3, "13" to 3, "14" to 2, "15" to 2,
        "16" to 1, "17" to 1
    )

    // 任二直選和值投注數對照表
    private val anotherTwoDirectSelectSumKeymap = mapOf(
        "0" to 1, "1" to 2, "2" to 3, "3" to 4, "4" to 5, "5" to 6,
        "6" to 7, "7" to 8, "8" to 9, "9" to 10, "10" to 9,
        "11" to 8, "12" to 7, "13" to 6, "14" to 5, "15" to 4,
        "16" to 3, "17" to 2, "18" to 1
    )

    // 任二組選和值投注數對照表
    private val anotherTwoCombineSelectSumKeymap = mapOf(
        "1" to 1, "2" to 1, "3" to 2, "4" to 2, "5" to 3,
        "6" to 3, "7" to 4, "8" to 4, "9" to 5, "10" to 4,
        "11" to 4, "12" to 3, "13" to 3, "14" to 2, "15" to 2,
        "16" to 1, "17" to 1
    )

    // 任三直選和值投注數對照表
    private val anotherThreeDirectSelectSumKeymap = mapOf(
        "0" to 1, "1" to 3, "2" to 6, "3" to 10, "4" to 15, "5" to 21,
        "6" to 28, "7" to 36, "8" to 45, "9" to 55, "10" to 63, "11" to 69,
        "12" to 73, "13" to 75, "14" to 75, "15" to 73, "16" to 69, "17" to 63,
        "18" to 55, "19" to 45, "20" to 36, "21" to 28, "22" to 21, "23" to 15,
        "24" to 10, "25" to 6, "26" to 3, "27" to 1
    )

    // 任三組選和值投注數對照表
    private val anotherThreeCombineSelectSumKeymap = mapOf(
        "1" to 1, "2" to 2, "3" to 2, "4" to 4, "5" to 5,
        "6" to 6, "7" to 8, "8" to 10, "9" to 11, "10" to 13,
        "11" to 14, "12" to 14, "13" to 15, "14" to 15, "15" to 14,
        "16" to 14, "17" to 13, "18" to 11, "19" to 10, "20" to 8,
        "21" to 6, "22" to 5, "23" to 4, "24" to 2, "25" to 2, "26" to 1
    )


    /**
     * 時時彩-五星投注數計算
     */
    private fun getMinuteLotteryFiveStarBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //五星-直選複式
            playTypeID_205000 -> {
                functionDirectSelectDuplex(betSelectNumber.betNumber)
            }
            //五星-直選單式
            playTypeID_205001 -> {
                functionDirectSelectSingle(betSelectNumber.betNumber)
            }
            //五星-直選組合
            playTypeID_205010 -> {
                functionDirectSelectCombine(betSelectNumber.betNumber)
            }
            //五星-組選120
            playTypeID_205021 -> {
                functionCombination(betSelectNumber.betNumber.length, 5)
            }
            // 五星-組選60
            // C(N-1，3)*n+C(N,3)*m
            // N:单号个数
            // n:单号中包含的二重号数字个数
            // m:单号中不包含的二重号数字个数
            playTypeID_205022 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 3) * n + functionCombination(N, 3) * m
            }
            // 五星-組選30
            // C(N-1，2)*n+C(N,2)*m
            // N:二重号个数
            // n:二重号中包含的单号数字个数
            // m:二重号中不包含的单号数字个数
            playTypeID_205023 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[0].length
                val n = betItemList[0].getAllItem().isSameCount(betItemList[1].getAllItem())
                val m = betItemList[1].length - n

                functionCombination(N - 1, 2) * n + functionCombination(N, 2) * m
            }
            // 五星-組選20
            // C(N-1，2)*n+C(N,2)*m
            // N:单重号个数
            // n：单号中包含的三重号数字个数
            // m：单号中不包含的三重号数字个数
            playTypeID_205024 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 2) * n + functionCombination(N, 2) * m
            }
            // 五星-組選10
            // C(N-1，1)*n+C(N,1)*m
            // N:二重号个数
            // n:二重号中包含的三重号数字个数
            // m:二重号中不包含的三重号数字个数
            playTypeID_205025 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 1) * n + functionCombination(N, 1) * m
            }
            // 五星-組選5
            // C(N-1，1)*n+C(N,1)*m
            // N:单号个数
            // n:单号中包含的四重号数字个数
            // m:单号中不包含的四重号数字个数
            playTypeID_205026 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 1) * n + functionCombination(N, 1) * m
            }

            // 五星-一帆風順、好事成雙、三星報喜，四季發財
            // 投注數字長度即為注數結果
            playTypeID_205051,
            playTypeID_205052,
            playTypeID_205053,
            playTypeID_205054 -> {
                betSelectNumber.betNumber.length
            }
            // 定位膽-五星定位膽
            playTypeID_205040 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += item.length
                }
                resultCount
            }

            //不定位-二碼不定 C(N，2)
            playTypeID_205042 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            //不定位-三碼不定 C(N,3)
            playTypeID_205043 -> {
                functionCombination(betSelectNumber.betNumber.length, 3)
            }
            else -> {
                0
            }

        }
    }

    /**
     * 時時彩-四星投注數計算
     */
    private fun getMinuteLotteryFourStarBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //四星-直選複式(前四、後四)
            playTypeID_204100,
            playTypeID_204200 -> {
                functionDirectSelectDuplex(betSelectNumber.betNumber)
            }
            playTypeID_204101,
            playTypeID_204201 -> {
                functionDirectSelectSingle(betSelectNumber.betNumber)
            }
            //四星-直選組合(前四、後四)
            playTypeID_204110,
            playTypeID_204210 -> {
                functionDirectSelectCombine(betSelectNumber.betNumber)
            }
            // 四星-組選24
            // C（n，4）n≥5
            playTypeID_204121,
            playTypeID_204221 -> {
                functionCombination(betSelectNumber.betNumber.length, 4)
            }
            // 四星-組選12
            // C(N-1，2)*n+C(N,2)*m
            // N:单重号个数
            // n:单号中包含的二重号数字个数
            // m:单号中不包含的二重号数字个数
            playTypeID_204122,
            playTypeID_204222 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 2) * n + functionCombination(N, 2) * m
            }
            // 四星-組選6
            // C(N,2)
            playTypeID_204123,
            playTypeID_204223 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            // 四星-組選4
            // C(N-1，1)*n+C(N,1)*m
            // N:单号个数
            // n:单号中包含的三重号数字个数
            // m:单号中不包含的三重号数字个数
            playTypeID_204124,
            playTypeID_204224 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")

                val N = betItemList[1].length
                val n = betItemList[1].getAllItem().isSameCount(betItemList[0].getAllItem())
                val m = betItemList[0].length - n

                functionCombination(N - 1, 1) * n + functionCombination(N, 1) * m
            }
            //不定位-(前後)四一碼 C(N，1)
            playTypeID_204141,
            playTypeID_204241 -> {
                betSelectNumber.betNumber.length
            }
            //不定位-(前後)四二碼 C(N，2)
            playTypeID_204142,
            playTypeID_204242 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            //任選四-直選複式
            //公式 N1*N2*N3*N4 + N1*N2*N3*N5 + N1*N2*N4*N5 + N1*N3*N4*N5 + N2*N3*N4*N5
            playTypeID_204000 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val n1 = betItemList[0].length
                val n2 = betItemList[1].length
                val n3 = betItemList[2].length
                val n4 = betItemList[3].length
                val n5 = betItemList[4].length

                n1 * n2 * n3 * n4 + n1 * n2 * n3 * n5 + n1 * n2 * n4 * n5 + n1 * n3 * n4 * n5 + n2 * n3 * n4 * n5
            }
            //任選四-直選單式
            //公式 C(N,4)*M N：位置选择位数 M：手动输入注数
            playTypeID_204001 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 4) * betDataCount.second
            }
            //任選四-組選24
            //公式 C(N,4)*C(M,4) N：位置选择位数 M：手动输入注数
            playTypeID_204021 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 4) * functionCombination(betDataCount.second, 4)
            }
            //任選四-組選12
            //公式 C(M,4)*[C(N-1，2)*n+C(N,2)*m]
            //M：位置选择数
            //N:单重号个数
            //n：单号中包含的二重号数字个数
            //m：单号中不包含的二重号数字个数
            playTypeID_204022 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betNumberList = betItemList[1].split(",")

                val M: Int = betItemList[0].split(",").size
                val N = betNumberList[1].length
                val n = betNumberList[1].getAllItem().isSameCount(betNumberList[0].getAllItem())
                val m = betNumberList[0].length - n

                functionCombination(M, 4) * (functionCombination(N - 1, 2) * n + functionCombination(N, 2) * m)
            }
            //任選四-組選6
            //公式 C(M,4)*C(N,2) M：位置选择位数 N：数字个数
            playTypeID_204023 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 4) * functionCombination(betDataCount.second, 2)
            }
            //任選四-組選4
            //C(M,4)*[C(N-1，1)*n+C(N,1)*m]
            //M：位置选择数
            //N:单号个数
            //n：单号中包含的三重号数字个数
            //m：单号中不包含的三重号数字个数
            playTypeID_204024 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betNumberList = betItemList[1].split(",")

                val M: Int = betItemList[0].split(",").size
                val N = betNumberList[1].length
                val n = betNumberList[1].getAllItem().isSameCount(betNumberList[0].getAllItem())
                val m = betNumberList[0].length - n

                functionCombination(M, 4) * (functionCombination(N - 1, 1) * n + functionCombination(N, 1) * m)
            }
            else -> {
                0
            }
        }
    }

    /**
     * 時時彩-三星投注數計算
     */
    private fun getMinuteLotteryThreeStarBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //三星-直選複式(前三、中三、後三)
            playTypeID_203100,
            playTypeID_203200,
            playTypeID_203300 -> {
                functionDirectSelectDuplex(betSelectNumber.betNumber)
            }
            //三星-直選單式(前三、中三、後三)
            playTypeID_203101,
            playTypeID_203201,
            playTypeID_203301 -> {
                functionDirectSelectSingle(betSelectNumber.betNumber)
            }
            //三星-直選組合(前三、中三、後三)
            playTypeID_203110,
            playTypeID_203210,
            playTypeID_203310 -> {
                functionDirectSelectCombine(betSelectNumber.betNumber)
            }
            //三星-直選和值(前三、中三、後三)
            playTypeID_203102,
            playTypeID_203202,
            playTypeID_203302 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += threeStarDirectSelectSumKeymap[item] ?: 0
                }
                resultCount
            }
            //三星-直選跨度(前三、中三、後三)
            playTypeID_203103,
            playTypeID_203203,
            playTypeID_203303 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += threeStarDirectSelectSpanKeymap[item] ?: 0
                }
                resultCount
            }
            //三星-組選組三複式(前三、中三、後三) C(N,2)*C(2，1)
            playTypeID_203123,
            playTypeID_203223,
            playTypeID_203323 -> {
                functionCombination(betSelectNumber.betNumber.length, 2) * 2
            }
            //三星-組選組三單式(前三、中三、後三)
            playTypeID_203121,
            playTypeID_203221,
            playTypeID_203321 -> {
                betSelectNumber.betNumber.split(",").size
            }
            //三星-組選組六複式(前三、中三、後三) C(N,3)
            playTypeID_203124,
            playTypeID_203224,
            playTypeID_203324 -> {
                functionCombination(betSelectNumber.betNumber.length, 3)
            }
            //三星-組選組六單式(前三、中三、後三)
            playTypeID_203122,
            playTypeID_203222,
            playTypeID_203322 -> {
                betSelectNumber.betNumber.split(",").size
            }
            //三星-組選和值(前三、中三、後三)
            playTypeID_203131,
            playTypeID_203231,
            playTypeID_203331 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += threeStarCombineSelectSumKeymap[item] ?: 0
                }
                resultCount
            }
            //三星-組選包膽(前三、中三、後三)
            playTypeID_203133,
            playTypeID_203233,
            playTypeID_203333 -> {
                betSelectNumber.betNumber.split(",").size * 54
            }
            //三星-和值尾数(前三、中三、後三)
            playTypeID_203134,
            playTypeID_203234,
            playTypeID_203334 -> {
                betSelectNumber.betNumber.length
            }
            //三星-其他特殊號碼(豹子、順子、對子)(前三、中三、後三)
            playTypeID_203135,
            playTypeID_203235,
            playTypeID_203335 -> {
                betSelectNumber.betNumber.split(",").size
            }
            //不定位-(前中後)三一碼 C(N，1)
            playTypeID_203140,
            playTypeID_203240,
            playTypeID_203340 -> {
                betSelectNumber.betNumber.length
            }
            //不定位-(前中後)三二碼 C(N，2)
            playTypeID_203142,
            playTypeID_203242,
            playTypeID_203342 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            //大小單雙-(前三、中三、後三) N1*N2*N3
            playTypeID_203150,
            playTypeID_203250,
            playTypeID_203350 -> {
                var resultCount: Int = 1
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount *= item.length
                }
                resultCount
            }
            //任選三-直選複式
            //公式 N1*N2*(N3+N4+N5)+N1*N3*(N4+N5)+(N1+N2+N3)*N4*N5+N2*N3*(N4+N5)
            playTypeID_203000 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val n1 = betItemList[0].length
                val n2 = betItemList[1].length
                val n3 = betItemList[2].length
                val n4 = betItemList[3].length
                val n5 = betItemList[4].length

                n1 * n2 * (n3 + n4 + n5) + n1 * n3 * (n4 + n5) + (n1 + n2 + n3) * n4 * n5 + n2 * n3 * (n4 + n5)
            }
            //任選三-直選單式
            //公式 C(N,3)*M N：位置选择位数 M：手动填写区有效注数
            playTypeID_203001 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 3) * betDataCount.second
            }
            //任選三-直選和值
            playTypeID_203002 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")
                var multiplySum: Int = 0
                for (item in betNumberList) {
                    multiplySum += anotherThreeDirectSelectSumKeymap[item] ?: 0
                }

                functionCombination(betUnitList.size, 3) * multiplySum
            }
            //任選三-組三複式、組三單式
            //公式 C(N,3)*C(M,2)*C(2，1) N：位置选择位数 M：号码选择数
            playTypeID_203023,
            playTypeID_203021 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 3) * functionCombination(betDataCount.second, 2) * 2
            }
            //任選三-組六複式、組六單式
            //公式 C(N,3)*C(M,3) N：位置选择位数 M：号码选择数
            playTypeID_203024,
            playTypeID_203022 -> {
                val betDataCount = getBetUnitAndNumberListCount(betSelectNumber.betNumber)

                functionCombination(betDataCount.first, 3) * functionCombination(betDataCount.second, 3)
            }
            //任選三-組選和值
            playTypeID_203031 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")
                var multiplySum: Int = 0
                for (item in betNumberList) {
                    multiplySum += anotherThreeCombineSelectSumKeymap[item] ?: 0
                }

                functionCombination(betUnitList.size, 3) * multiplySum
            }
            else -> {
                0
            }
        }
    }

    /**
     * 時時彩-二星投注數計算
     */
    private fun getMinuteLotteryTwoStarBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //二星-直選複式(前二、後二) N1*N2
            playTypeID_202100,
            playTypeID_202200 -> {
                functionDirectSelectDuplex(betSelectNumber.betNumber)
            }
            //二星-直選單式(前二、後二)
            playTypeID_202101,
            playTypeID_202201 -> {
                functionDirectSelectSingle(betSelectNumber.betNumber)
            }
            //二星-直選和值(前二、後二)
            playTypeID_202102,
            playTypeID_202202 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += twoStarDirectSelectSumKeymap[item] ?: 0
                }
                resultCount
            }
            //二星-直選跨度(前二、後二)
            playTypeID_202103,
            playTypeID_202203 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += twoStarDirectSelectSpanKeymap[item] ?: 0
                }
                resultCount
            }
            //二星-組選複式(前二、後二) C(N,2）
            playTypeID_202110,
            playTypeID_202210 -> {
                functionCombination(betSelectNumber.betNumber.length, 2)
            }
            //二星-組選單式(前二、後二)
            playTypeID_202120,
            playTypeID_202220 -> {
                functionDirectSelectSingle(betSelectNumber.betNumber)
            }
            //二星-組選和值(前二、後二)
            playTypeID_202131,
            playTypeID_202231 -> {
                var resultCount: Int = 0
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount += twoStarCombineSelectSumKeymap[item] ?: 0
                }
                resultCount
            }
            //二星-組選包膽(前二、後二) 从0-9，每个号码都是9注
            playTypeID_202133,
            playTypeID_202233 -> {
                betSelectNumber.betNumber.split(",").size * 9
            }
            //大小單雙-(前二、後二) N1*N2
            playTypeID_202150,
            playTypeID_202250 -> {
                var resultCount: Int = 1
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                for (item in betItemList) {
                    resultCount *= item.length
                }
                resultCount
            }
            //任選二-直選複式
            //公式 N1*(N2+N3+N4+N5) + N2*(N3+N4+N5) + N3*(N4+N5) + N4*N5
            playTypeID_202000 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split(",")
                val n1 = betItemList[0].length
                val n2 = betItemList[1].length
                val n3 = betItemList[2].length
                val n4 = betItemList[3].length
                val n5 = betItemList[4].length

                n1 * (n2 + n3 + n4 + n5) + n2 * (n3 + n4 + n5) + n3 * (n4 + n5) + n4 * n5
            }
            //任選二-直選單式
            //公式 C(N,2)*M  N：位置选择位数 M：手动填写区有效注数
            playTypeID_202001 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")

                functionCombination(betUnitList.size, 2) * betNumberList.size
            }
            //任選二-直選和值
            playTypeID_202002 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")
                var multiplySum: Int = 0
                for (item in betNumberList) {
                    multiplySum += anotherTwoDirectSelectSumKeymap[item] ?: 0
                }

                functionCombination(betUnitList.size, 2) * multiplySum
            }
            //任選二-組選複式
            //公式 C(N,2)*C(M,2) N：位置选择位数 M：号码选择数
            playTypeID_202010 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberLength: Int = betItemList[1].length

                functionCombination(betUnitList.size, 2) * functionCombination(betNumberLength, 2)
            }
            //任選二-組選單式
            //公式 C(N,2)*M N：位置选择位数 M：手动填写区有效注数
            playTypeID_202020 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")

                functionCombination(betUnitList.size, 2) * betNumberList.size
            }
            //任選二-組選和值
            playTypeID_202031 -> {
                val betItemList: List<String> = betSelectNumber.betNumber.split("@")
                val betUnitList = betItemList[0].split(",")
                val betNumberList = betItemList[1].split(",")
                var multiplySum: Int = 0
                for (item in betNumberList) {
                    multiplySum += anotherTwoCombineSelectSumKeymap[item] ?: 0
                }

                functionCombination(betUnitList.size, 2) * multiplySum
            }
            else -> {
                0
            }
        }
    }

    private fun getMinuteLotteryOtherBetCount(betSelectNumber: BetSelectNumber): Int {
        return when (betSelectNumber.playTypeCode) {
            //龍虎和
            playTypeID_206010 -> {
                val betItemList = betSelectNumber.betNumber.replace(",", "").split("@")
                var resultCount: Int = 1
                for (item in betItemList) {
                    resultCount *= item.length
                }
                resultCount
            }
            else -> {
                0
            }
        }
    }

    /**
     * 計算公式(直選複式)-將betNumberList以“,”拆解為list後，再依序做相乘的動作，相乘數即為結果數。
     */
    private fun functionDirectSelectDuplex(betNumberList: String): Int {
        var resultCount: Int = 0
        val betItemList: List<String> = betNumberList.split(",")
        for (i in 0 until betItemList.size) {
            if (i == 0) {
                resultCount += betItemList[i].length
            } else {
                resultCount *= betItemList[i].length
            }
        }
        return resultCount
    }

    /**
     * 計算公式(直選單式)-將betNumberList以“,”拆解為list後，list總數即為結果數。
     */
    private fun functionDirectSelectSingle(betNumberList: String): Int {
        var resultCount: Int = 0
        val betItemList: List<String> = betNumberList.split(",")
        resultCount = betItemList.size
        return resultCount
    }

    /**
     * 計算公式(直選組合)-將betNumberList以“,”拆解為list後，再依序做相乘的動作，最後再乘於list總數即爲結果數。
     */
    private fun functionDirectSelectCombine(betNumberList: String): Int {
        var resultCount: Int = 0

        val betItemList: List<String> = betNumberList.split(",")
        for (i in 0 until betItemList.size) {
            if (resultCount == 0) {
                resultCount += betItemList[i].length
            } else {
                resultCount *= betItemList[i].length
            }
        }
        resultCount *= betItemList.size
        return resultCount
    }

    /**
     * 應對 1,2,3,4@11,3,4,6 的投注字串進行單位選擇和數字選擇的解析 回傳這兩組字串的結果數
     *
     * @return Pair<betUnitCount,betNumberCount>
     */
    private fun getBetUnitAndNumberListCount(betStr: String): Pair<Int, Int> {
        val betItemList: List<String> = betStr.split("@")
        val betUnitListCount =
            if (betItemList[0].contains(",")) betItemList[0].split(",").size else betItemList[0].length
        val betNumberListCount =
            if (betItemList[1].contains(",")) betItemList[1].split(",").size else betItemList[1].length

        return Pair(betUnitListCount, betNumberListCount)
    }

    private fun List<String>.isSameCount(compareList: List<String>): Int {
        var count = 0
//        compareList.forEach { if (this.contains(it)) count++ }
        for (item in compareList) {
            if (this.contains(item)) count++
        }
        return count
    }

    private fun String.getAllItem(): ArrayList<String> {
        val itemList: ArrayList<String> = arrayListOf()
        for (i in 0 until this.length) {
            itemList.add(this.substring(i, i + 1))
        }
        return itemList
    }

    private fun a(n: Int, m: Int): Int {
        var q = n

        var result = 1
        // 循环m次,如A(6,2)需要循环2次，6*5
        for (i in m downTo 1) {
            result *= q
            q--// 下一次减一
        }
        return result
    }

    /**
     * 計算公式(c幾取幾)
     *
     * @param n 總數
     * @param m 組合數
     */
    private fun functionCombination(n: Int, m: Int): Int {
        val helf = n / 2
        var q = m

        if (q > helf) {
            q = n - m
        }
        val numerator = a(n, m)
        val denominator = a(m, m)
        return numerator / denominator
    }
}