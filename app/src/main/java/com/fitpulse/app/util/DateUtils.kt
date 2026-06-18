package com.fitpulse.app.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class StatsPeriod(val label: String) {
    DAY("Day"),
    WEEK("Week"),
    MONTH("Month"),
    TOTAL("Total")
}

data class PeriodRange(
    val start: Long,
    val endExclusive: Long,
    val label: String
)

object DateUtils {

    private val dayLabelFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
    private val dayShortFormat = SimpleDateFormat("dd MMM", Locale.getDefault())
    private val monthFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())

    /** Epoch millis -> human day label, e.g. "Mon, 18 Jun 2026". */
    fun dayLabel(epochMillis: Long): String = dayLabelFormat.format(Date(epochMillis))

    /** Start of the day (00:00) for the given epoch millis. */
    fun startOfDay(epochMillis: Long): Long = calendarAt(epochMillis).apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    /** The date range + label for [period] anchored on [anchor]. */
    fun periodRange(period: StatsPeriod, anchor: Long): PeriodRange {
        return when (period) {
            StatsPeriod.DAY -> {
                val start = startOfDay(anchor)
                val end = calendarAt(start).apply { add(Calendar.DAY_OF_YEAR, 1) }.timeInMillis
                PeriodRange(start, end, dayLabel(start))
            }
            StatsPeriod.WEEK -> {
                val cal = calendarAt(startOfDay(anchor))
                cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
                val start = cal.timeInMillis
                val endCal = calendarAt(start).apply { add(Calendar.DAY_OF_YEAR, 7) }
                val end = endCal.timeInMillis
                val lastDay = calendarAt(end).apply { add(Calendar.DAY_OF_YEAR, -1) }.timeInMillis
                val label = "${dayShortFormat.format(Date(start))} - " +
                    "${dayShortFormat.format(Date(lastDay))} ${yearFormat.format(Date(lastDay))}"
                PeriodRange(start, end, label)
            }
            StatsPeriod.MONTH -> {
                val cal = calendarAt(startOfDay(anchor)).apply {
                    set(Calendar.DAY_OF_MONTH, 1)
                }
                val start = cal.timeInMillis
                val end = calendarAt(start).apply { add(Calendar.MONTH, 1) }.timeInMillis
                PeriodRange(start, end, monthFormat.format(Date(start)))
            }
            StatsPeriod.TOTAL -> PeriodRange(0L, Long.MAX_VALUE, "All time")
        }
    }

    /** Move the anchor by one unit of [period] in [direction] (-1 = back, +1 = forward). */
    fun stepAnchor(period: StatsPeriod, anchor: Long, direction: Int): Long {
        val cal = calendarAt(anchor)
        when (period) {
            StatsPeriod.DAY -> cal.add(Calendar.DAY_OF_YEAR, direction)
            StatsPeriod.WEEK -> cal.add(Calendar.DAY_OF_YEAR, 7 * direction)
            StatsPeriod.MONTH -> cal.add(Calendar.MONTH, direction)
            StatsPeriod.TOTAL -> {}
        }
        return cal.timeInMillis
    }

    private fun calendarAt(epochMillis: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = epochMillis }
}
