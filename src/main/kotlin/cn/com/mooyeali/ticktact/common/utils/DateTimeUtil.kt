package cn.com.mooyeali.ticktact.common.utils

import cn.com.mooyeali.ticktact.common.annotations.Slf4k
import cn.com.mooyeali.ticktact.common.annotations.Slf4k.Companion.log
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.floor


/**
 * 日期时间工具类<br></br>
 * 提供一些常用的日期时间操作方法，所有方法都为静态，不用实例化该类即可使用。<br></br>
 * <br></br>
 * 下为日期格式的简单描述详情请参看java API中java.text.SimpleDateFormat<br></br>
 */
@Slf4k
object DateTimeUtil {
    /**
     * 缺省的日期显示格式： yyyy-MM-dd
     */
    const val DEFAULT_DATE_FORMAT: String = "yyyy-MM-dd"

    /**
     * 缺省的日期时间显示格式：yyyy-MM-dd HH:mm:ss
     */
    const val DEFAULT_DATETIME_FORMAT: String = "yyyy-MM-dd HH:mm:ss"

    val now: Date
        /**
         * 得到系统当前日期时间
         *
         * @return 当前日期时间
         */
        get() = Calendar.getInstance().time

    val date: String
        /**
         * 得到用缺省方式格式化的当前日期
         *
         * @return 当前日期
         */
        get() = getDateTime(DEFAULT_DATE_FORMAT)

    val dateTime: String
        /**
         * 得到用缺省方式格式化的当前日期及时间
         *
         * @return 当前日期及时间
         */
        get() = getDateTime(DEFAULT_DATETIME_FORMAT)

    val dateTimeNoSeparater: String
        get() = getDateTime(DEFAULT_DATETIME_FORMAT)


    /**
     * 得到系统当前日期及时间，并用指定的方式格式化
     *
     * @param pattern 显示格式
     * @return 当前日期及时间
     */
    fun getDateTime(pattern: String?): String {
        val datetime = Calendar.getInstance().time

        return getDateTime(datetime, pattern)
    }

    /**
     * 得到用指定方式格式化的日期
     *
     * @param date    需要进行格式化的日期
     * @param pattern 显示格式
     * @return 日期时间字符串
     */
    fun getDateTime(date: Date?, pattern: String?): String {
        var pattern = pattern
        if (null == pattern || "" == pattern) {
            pattern = DEFAULT_DATETIME_FORMAT
        }
        val dateFormat = SimpleDateFormat(pattern)
        return dateFormat.format(date)
    }

    val currentYear: Int
        /**
         * 得到当前年份
         *
         * @return 当前年份
         */
        get() = Calendar.getInstance()[Calendar.YEAR]

    val currentMonth: Int
        /**
         * 得到当前月份
         *
         * @return 当前月份
         */
        get() =// 用get得到的月份数比实际的小1，需要加上
            Calendar.getInstance()[Calendar.MONTH] + 1

    val currentDay: Int
        /**
         * 得到当前日
         *
         * @return 当前日
         */
        get() = Calendar.getInstance()[Calendar.DATE]

    /**
     * 取得当前日期以后若干天的日期。如果要得到以前的日期，参数用负数。 例如要得到上星期同一天的日期，参数则为-7
     *
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    fun addDays(days: Int): Date {
        return add(now, days, Calendar.DATE)
    }

    /**
     * 取得指定时间以后若干秒。如果要得到之前的秒数，参数用负数。
     *
     * @param date 基准时间
     * @param days 增加的时间数
     * @return 增加以后的时间
     */
    fun addSeconds(date: Date, days: Int): Date {
        return add(date, days, Calendar.SECOND)
    }

    /**
     * 取得指定日期以后若干天的日期。如果要得到以前的日期，参数用负数。
     *
     * @param date 基准日期
     * @param days 增加的日期数
     * @return 增加以后的日期
     */
    fun addDays(date: Date, days: Int): Date {
        return add(date, days, Calendar.DATE)
    }

    /**
     * 取得当前日期以后某月的日期。如果要得到以前月份的日期，参数用负数。
     *
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    fun addMonths(months: Int): Date {
        return add(now, months, Calendar.MONTH)
    }

    /**
     * 取得指定日期以后某月的日期。如果要得到以前月份的日期，参数用负数。 注意，可能不是同一日子，例如2003-1-31加上一个月是2003-2-28
     *
     * @param date   基准日期
     * @param months 增加的月份数
     * @return 增加以后的日期
     */
    fun addMonths(date: Date, months: Int): Date {
        return add(date, months, Calendar.MONTH)
    }

    /**
     * 内部方法。为指定日期增加相应的天数或月数
     *
     * @param date   基准日期
     * @param amount 增加的数量
     * @param field  增加的单位，年，月或者日
     * @return 增加以后的日期
     */
    private fun add(date: Date, amount: Int, field: Int): Date {
        val calendar = Calendar.getInstance()

        calendar.time = date
        calendar.add(field, amount)

        return calendar.time
    }

    /**
     * 计算两个日期相差天数。 用第一个日期减去第二个。如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差天数
     */
    fun diffDays(one: Date, two: Date): Long {
        return (one.time - two.time) / (24 * 60 * 60 * 1000)
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差月份数
     */
    fun diffMonths(one: Date, two: Date): Int {
        val calendar = Calendar.getInstance()

        // 得到第一个日期的年分和月份数
        calendar.time = one
        val yearOne = calendar[Calendar.YEAR]
        val monthOne = calendar[Calendar.MONDAY]

        // 得到第二个日期的年份和月份
        calendar.time = two
        val yearTwo = calendar[Calendar.YEAR]
        val monthTwo = calendar[Calendar.MONDAY]

        return (yearOne - yearTwo) * 12 + (monthOne - monthTwo)
    }

    /**
     * 计算两个日期相差月份数 如果前一个日期小于后一个日期，则返回负数
     *
     * @param one 第一个日期数，作为基准
     * @param two 第二个日期数，作为比较
     * @return 两个日期相差分钟
     */
    fun diffMinutes(one: Date, two: Date): Int {
        val calendar = Calendar.getInstance()

        // 得到第一个日期的年分和月份数
        calendar.time = one
        val dayOfMonthOne = calendar[Calendar.DAY_OF_MONTH]
        val hourOne = calendar[Calendar.HOUR]
        val minuteOne = calendar[Calendar.MINUTE]
        val secondOne = calendar[Calendar.SECOND]

        // 得到第二个日期的年份和月份
        calendar.time = two
        val dayOfMonthTwo = calendar[Calendar.DAY_OF_MONTH]
        val hourTwo = calendar[Calendar.HOUR]
        val minuteTwo = calendar[Calendar.MINUTE]
        val secondTwo = calendar[Calendar.SECOND]

        return (((dayOfMonthOne * 24 + hourOne) * 3600 + minuteOne * 60 + secondOne) - ((dayOfMonthTwo * 24 + hourTwo) * 3600 + minuteTwo * 60 + secondTwo)) / 60
    }


    /**
     * 将一个字符串用给定的格式转换为日期类型。<br></br>
     * 注意：如果返回null，则表示解析失败
     *
     * @param datestr 需要解析的日期字符串
     * @param pattern 日期字符串的格式，默认为“yyyy-MM-dd”的形式
     * @return 解析后的日期
     */
    fun parse(datestr: String?, pattern: String?): Date? {
        var pattern = pattern
        var date: Date? = null

        if (null == pattern || "" == pattern) {
            pattern = DEFAULT_DATE_FORMAT
        }

        try {
            val dateFormat = SimpleDateFormat(pattern)
            date = dateFormat.parse(datestr)
        } catch (e: ParseException) {
            //
        }

        return date
    }

    val monthLastDay: Date
        /**
         * 返回本月的最后一天
         *
         * @return 本月最后一天的日期
         */
        get() = getMonthLastDay(now)

    /**
     * 返回给定日期中的月份中的最后一天
     *
     * @param date 基准日期
     * @return 该月最后一天的日期
     */
    fun getMonthLastDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date

        // 将日期设置为下一月第一天
        calendar[calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1] = 1

        // 减去1天，得到的即本月的最后一天
        calendar.add(Calendar.DATE, -1)

        return calendar.time
    }

    /**
     * 返回给定日期中的月份
     *
     * @param date 基准日期
     * @return 月份
     */
    fun getMonth(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date

        return calendar[Calendar.MONTH] + 1
    }

    /**
     * 返回给定日期中的月份
     *
     * @param date 基准日期
     * @return 月份
     */
    fun getMonthString(date: Date): String {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val month = calendar[Calendar.MONTH] + 1
        val strbuf2 = StringBuffer()
        if (month < 10) strbuf2.append("0")
        strbuf2.append(month)

        return strbuf2.toString()
    }

    /**
     * 返回给定日期中的年份
     *
     * @param date 基准日期
     * @return 年份
     */
    fun getYear(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR]
    }

    /**
     * 传递一个long型的数据,返回格式化时间,例"1-28 11:47:1"
     *
     * @param myDate
     * @return
     */
    fun longDateYMDT(myDate: Long): String {
        if (myDate > 0) {
            val cal = GregorianCalendar()
            cal.timeInMillis = myDate

            return cal[1].toString() + "-" + (cal[2] + 1) + "-" + cal[5] + " " + cal[11] + ":" + cal[12] + ":" + cal[13]
        } else {
            return ""
        }
    }

    /**
     * 传递一个long型的数据,返回格式化时间,例"2005-1-28 11:47:1"
     *
     * @param myDate
     * @return
     */
    fun longDateMDT(myDate: Long): String {
        if (myDate > 0) {
            val cal = GregorianCalendar()
            cal.timeInMillis = myDate

            return (cal[2] + 1).toString() + "-" + cal[5] + " " + cal[11] + ":" + cal[12] + ":" + cal[13]
        } else {
            return ""
        }
    }

    /**
     * 传递一个long型的数据,返回格式化时间,例"2005-01-28"
     *
     * @param myDate
     * @return
     */
    fun shortDateYMD(myDate: Long): String {
        if (myDate > 0) {
            val cal = GregorianCalendar()
            cal.timeInMillis = myDate

            var mm = "00" + (cal[2] + 1)
            mm = mm.substring(mm.length - 2)

            var dd = "00" + cal[5]
            dd = dd.substring(dd.length - 2)
            return cal[1].toString() + "-" + mm + "-" + dd
        } else {
            return ""
        }
    }

    /**
     * 传递一个long型的数据,返回格式化时间,例"1-28"
     *
     * @param myDate
     * @return
     */
    fun shortDateMD(myDate: Long): String {
        if (myDate > 0) {
            val cal = GregorianCalendar()
            cal.timeInMillis = myDate

            return (cal[2] + 1).toString() + "-" + cal[5]
        } else {
            return ""
        }
    }

    /**
     * 把秒总数转换为小时：分钟：秒格式
     *
     * @param minute
     * @return
     */
    fun Second2Time(minute: Long): String {
        val h = floor((minute / 3600).toDouble()) as Int

        var hours = "00$h"
        hours = hours.substring(hours.length - 2) + ":"

        val m = floor(((minute - (h * 3600L)) / 60).toDouble()) as Int
        var minutes = "00$m"
        minutes = minutes.substring(minutes.length - 2) + ":"

        val s = floor((minute - (h * 3600L) - (m * 60L)).toDouble()) as Int
        var seconds = "00$s"
        seconds = seconds.substring(seconds.length - 2)

        return hours + minutes + seconds
    }

    /**
     * 把时间格式（小时：分钟：秒）转换为int秒
     *
     * @param time
     * @return int
     */
    fun Time2Second(time: String): Int {
        val pairs = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

        for (j in pairs.indices) {
            if ("0" == pairs[j].substring(0, 1)) {
                pairs[j] = pairs[j].substring(1)
            }

            pairs[j] = pairs[j]
        }

        return (pairs[0].toInt() * 3600) + (pairs[1].toInt() * 60) + pairs[2].toInt()
    }

    /**
     * 把 数字格式化成string，可满足保留小数的需要
     *
     * @param args
     */
    fun decimalFormatString(dou: Double, type: String): String {
        val formatter: NumberFormat = DecimalFormat(type)
        return formatter.format(dou)
    }

    /* 转换时间格式   yyyyMMddHHmmss ——> yyyy-MM-dd HH:mm:ss */
    fun convertStringDate(myDate: String?, pattern: String): String? {
        var now: String? = null
        try {
            val date = SimpleDateFormat(pattern).parse(myDate)
            now = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
        } catch (e: Exception) {
            log.error("转换时间格式错误,错误信息:{}", e.message, e)
        }

        return now
    }

    /**
     * 转换时间格式
     *
     * @param myDate     时间变量
     * @param oldpattern 原时间格式
     * @param pattern    转换后的格式
     * @return
     */
    fun convertStringDate(myDate: String?, oldpattern: String, pattern: String): String? {
        var now: String? = null
        try {
            val date = SimpleDateFormat(oldpattern).parse(myDate)
            now = SimpleDateFormat(pattern).format(date)
        } catch (e: Exception) {
            log.error("转换时间格式错误,错误信息:{}", e.message, e)
        }

        return now
    }

    @JvmStatic
    fun main(args: Array<String>) {
//		String test = "2007-01-31 10:10:10";
//		Date date;
//		String test1 = "2009-10-16 09:28:39";
//		String test2 = "2009-10-16 09:28:29";
//		Date date1;
//		Date date2;
        val myDate = "20120525121901"
        try {
//			date = parse(test, "");
//
//			System.out.println("得到当前日期 － getDate():" + DateTimeUtil.getDate());
            println("得到当前日期时间 － getDateTime():" + dateTime)

            //
//			System.out.println("得到当前年份 － getCurrentYear():" + DateTimeUtil.getCurrentYear());
//			System.out.println("得到当前月份 － getCurrentMonth():" + DateTimeUtil.getCurrentMonth());
//			System.out.println("得到当前日子 － getCurrentDay():" + DateTimeUtil.getCurrentDay());
//
//			System.out.println("解析 － parse(" + test + "):" + getDateTime(date, "yyyy-MM-dd"));
//
//			System.out.println("自增月份 － addMonths(3):" + getDateTime(addMonths(12), "yyyy-MM-dd"));
//			System.out.println("增加月份 － addMonths(" + test + ",3):" + getDateTime(addMonths(date, 3), "yyyy-MM-dd"));
//			System.out.println("增加日期 － addDays(" + test + ",3):" + getDateTime(addDays(date, 3), "yyyy-MM-dd"));
//			System.out.println("自增日期 － addDays(3):" + getDateTime(addDays(3), "yyyy-MM-dd"));
//			System.out.println("自增秒数 － addSeconds(" + test + ",3):" + getDateTime(addSeconds(date, 3), "yyyy-MM-dd HH:mm:ss"));
//
//			System.out.println("比较日期 － diffDays():" + DateTimeUtil.diffDays(DateTimeUtil.getNow(), date));
//			System.out.println("比较月份 － diffMonths():" + DateTimeUtil.diffMonths(DateTimeUtil.getNow(), date));
//
//			System.out.println("得到" + test + "所在月份的最后一天:" + getDateTime(getMonthLastDay(date), "yyyy-MM-dd"));
//			System.out.println("得到...."+getDateTime("yyyyMMdd"));
//
//
//			date1 = parse(test1, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//			date2 = parse(test2, DateTimeUtil.DEFAULT_DATETIME_FORMAT);
//			System.out.println("比较两个日期相差的分钟时间" + diffMinutes(date1,date2));
//			Date date = new SimpleDateFormat("yyyyMMddHHmmss").parse(myDate);
//			String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//			System.out.println(now);
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
