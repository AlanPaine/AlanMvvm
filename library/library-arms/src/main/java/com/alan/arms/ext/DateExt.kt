package com.alan.arms.ext

import android.annotation.SuppressLint
import java.lang.Math.abs
import java.text.SimpleDateFormat
import java.util.*


/**         格式
 *
 *  yyyy    四位年
 *  yy      两位年
 *  MM      月份  始终两位
 *  M       月份
 *  dd      日期  始终两位
 *  d       日期
 *  HH      24小时制  始终两位
 *  H       24小时制
 *  hh      12小时制  始终两位
 *  h       12小时制
 *  mm      分钟  始终两位
 *  ss      秒  始终两位
 *
 */

/**
 * 年月日 时分秒
 */
const val YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss"

/**
 * 年月日 时分
 */
const val YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm"

/**
 * 年月日
 */
const val YYYY_MM_DD = "yyyy-MM-dd";

/**
 * 月日
 */
const val MM_DD = "MM-dd";

private val WEEKS = arrayOf("周日", "周一", "周二", "周三", "周四", "周五", "周六")

/**
 * Date格式化时间
 * @param format 格式
 */
@SuppressLint("SimpleDateFormat")
fun Date.formatString(format: String = YYYY_MM_DD): String {
    return SimpleDateFormat(format).format(this)
}

/**
 * Date转Calendar
 */
fun Date.toCalendar(): Calendar {
    return Calendar.getInstance().apply {
        time = this@toCalendar
    }
}

/**
 * String格式的时间转Long
 * @param format 格式
 */
fun String.formatLong(format: String = YYYY_MM_DD): Long? {
    return formatDate(format)?.time
}

/**
 * String格式的时间转Long
 * @param format 格式
 */
@SuppressLint("SimpleDateFormat")
fun String.formatDate(format: String = YYYY_MM_DD): Date? {
    return SimpleDateFormat(format).parse(this)
}

/**
 * 获取年
 */
fun Calendar.getYear(): Int = get(Calendar.YEAR)

/**
 * 获取月
 */
fun Calendar.getMonth(): Int = get(Calendar.MONTH) + 1

/**
 * 获取日
 */
fun Calendar.getDay(): Int = get(Calendar.DAY_OF_MONTH)

/**
 * 获取周
 */
fun Calendar.getWeek(): String = WEEKS[get(Calendar.DAY_OF_WEEK)]

/**
 * 获取相邻的Date
 */
private fun getNearestDate(field: Int, amount: Int): Date {
    return Date().toCalendar().apply {
        add(field, amount)
    }.time
}

/**
 * @return 获取几天之内的的Date
 * @param amount 之后为整数 之前为负数
 */
fun getNearestDayDate(amount: Int): Date? {
    return getNearestDate(Calendar.DAY_OF_MONTH, amount)
}

/**
 * @return 获取几周之内的的Date
 * @param amount 之后为整数 之前为负数
 */
fun getNearestWeekDate(amount: Int): Date? {
    return getNearestDate(Calendar.WEEK_OF_YEAR, amount)
}

/**
 * @return 获取几个月之内的日期
 * @param amount 之后为整数 之前为负数
 */
fun getNearestMonthDate(amount: Int): Date? {
    return getNearestDate(Calendar.MONTH, amount)
}

/**
 *
 * @return 获取几年之内的Date
 * @param amount 之后为整数 之前为负数
 */
fun getNearestYearDate(amount: Int): Date? {
    return getNearestDate(Calendar.YEAR, amount)
}

/**
 * 获取某个年份的Date
 * @param year
 * @return
 */
fun getYearDate(year: Int): Date {
    return Calendar.getInstance().apply {
        set(Calendar.YEAR, year)
        set(Calendar.MONTH, 0)
        set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
    }.time
}

/**
 * 获得两个日期间距多少天
 * @param begin 开始日期
 * @param end   结束日期
 */
fun getTimeDistance(begin: Date, end: Date = Date()): Int {
    return abs(((getDateTime(begin) - getDateTime(
        end
    )) / (1000 * 3600 * 24)).toInt())
}

/**
 * 获取一个Date的确切Long类型时间
 */
private fun getDateTime(date: Date): Long {
    return date.toCalendar().apply {
        set(Calendar.HOUR_OF_DAY, getMinimum(Calendar.HOUR_OF_DAY))
        set(Calendar.MINUTE, getMinimum(Calendar.MINUTE))
        set(Calendar.SECOND, getMinimum(Calendar.SECOND))
        set(Calendar.MILLISECOND, getMinimum(Calendar.MILLISECOND))
    }.time.time
}