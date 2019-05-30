@file:JvmName("DateUtils2")
package com.alamkanak.weekview.date

import android.content.Context
import android.text.format.DateFormat
import com.alamkanak.weekview.Constants.DAY_IN_MILLIS
import java.text.SimpleDateFormat
import java.util.*

internal val Calendar.hour: Int
    get() = get(Calendar.HOUR_OF_DAY)

internal val Calendar.minute: Int
    get() = get(Calendar.MINUTE)

internal val Calendar.dayOfWeek: Int
    get() {
        return when (val dayOfWeek = get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> DayOfWeek.MONDAY
            Calendar.TUESDAY -> DayOfWeek.TUESDAY
            Calendar.WEDNESDAY -> DayOfWeek.WEDNESDAY
            Calendar.THURSDAY -> DayOfWeek.THURSDAY
            Calendar.FRIDAY -> DayOfWeek.FRIDAY
            Calendar.SATURDAY -> DayOfWeek.SATURDAY
            Calendar.SUNDAY -> DayOfWeek.SUNDAY
            else -> throw IllegalStateException("Unknown day of week: $dayOfWeek")
        }
    }

internal val Calendar.month: Int
    get() = get(Calendar.MONTH)

internal val Calendar.year: Int
    get() = get(Calendar.YEAR)

internal fun Calendar.plusDays(days: Long): Calendar {
    return plusDays(days.toInt())
}

internal fun Calendar.isEqual(other: Calendar): Boolean {
    return timeInMillis == other.timeInMillis
}

internal fun Calendar.plusDays(days: Int): Calendar {
    return copy().apply {
        add(Calendar.DATE, days)
    }
}

internal fun Calendar.minusDays(days: Int): Calendar {
    return plusDays(days * (-1))
}

internal fun Calendar.plusMillis(millis: Int): Calendar {
    return copy().apply {
        add(Calendar.MILLISECOND, millis)
    }
}

internal fun Calendar.minusMillis(millis: Int): Calendar {
    return plusMillis(millis * (-1))
}

internal fun Calendar.isBefore(other: Calendar): Boolean {
    return timeInMillis < other.timeInMillis
}

internal fun Calendar.isAfter(other: Calendar): Boolean {
    return timeInMillis > other.timeInMillis
}

internal val Calendar.isBeforeToday: Boolean
    get() = isBefore(today())

internal val Calendar.isToday: Boolean
    get() = isSameDate(today())

internal fun Calendar.toEpochDays(): Int {
    return (timeInMillis / DAY_IN_MILLIS).toInt()
}

internal fun Calendar.lengthOfMonth(): Int = getActualMaximum(Calendar.DAY_OF_MONTH)

internal fun Calendar.withTimeAtStartOfPeriod(hour: Int): Calendar {
    return withHour(hour).withMinutes(0).withSeconds(0).withMillis(0)
}

internal fun Calendar.withTimeAtEndOfPeriod(hour: Int): Calendar {
    return withHour(hour - 1).withMinutes(59).withSeconds(59).withMillis(999)
}

internal val Calendar.atStartOfDay: Calendar
    get() {
        return withTimeAtStartOfPeriod(0)
    }

internal val Calendar.daysFromToday: Int
    get() {
        val diff = timeInMillis - today().timeInMillis
        return (diff / (DAY_IN_MILLIS)).toInt()
    }

internal fun today(): Calendar = now().atStartOfDay

internal fun now(): Calendar {
    return Calendar.getInstance() // TODO Timezone?
}

internal fun Calendar.isSameDate(other: Calendar): Boolean = toEpochDays() == other.toEpochDays()

internal fun firstDayOfYear(): Calendar {
    return today()
            .withMonth(Calendar.JANUARY)
            .withDayOfMonth(1)
}

internal fun getDateRange(daysSinceToday: Int, size: Int): List<Calendar> {
    return (daysSinceToday..size).map { today().plusDays(it - 1) }
}

internal val Calendar.isWeekend: Boolean
    get() = dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY

internal fun Calendar.withYear(year: Int): Calendar {
    return copy().apply { set(Calendar.YEAR, year) }
}

internal fun Calendar.withMonth(month: Int): Calendar {
    return copy().apply { set(Calendar.MONTH, month) }
}

internal fun Calendar.withDayOfMonth(day: Int): Calendar {
    return copy().apply { set(Calendar.DAY_OF_MONTH, day) }
}

internal fun Calendar.withTime(hour: Int, minutes: Int): Calendar {
    return withHour(hour).withMinutes(minutes).withSeconds(0).withMillis(0)
}

internal fun Calendar.withHour(hour: Int): Calendar {
    return copy().apply { set(Calendar.HOUR_OF_DAY, hour) }
}

internal fun Calendar.withMinutes(minute: Int): Calendar {
    return copy().apply { set(Calendar.MINUTE, minute) }
}

internal fun Calendar.withSeconds(seconds: Int): Calendar {
    return copy().apply { set(Calendar.SECOND, seconds) }
}

internal fun Calendar.withMillis(millis: Int): Calendar {
    return copy().apply { set(Calendar.MILLISECOND, millis) }
}

private fun Calendar.copy(): Calendar = clone() as Calendar

/**
 * Checks if this date is at the start of the next day after startTime.
 * For example, if the start date was January the 1st and startDate was January the 2nd at 00:00,
 * this method would return true.
 * @param startDate The start date of the event
 * @return Whether or not this date is at the start of the day after startDate
 */
internal fun Calendar.isAtStartOfNextDay(startDate: Calendar): Boolean {
    return if (isEqual(atStartOfDay)) {
        minusMillis(1).isSameDate(startDate)
    } else {
        false
    }
}

internal fun getDefaultDateFormat(numberOfDays: Int): SimpleDateFormat {
    return when (numberOfDays) {
        7 -> SimpleDateFormat("EEEEE M/dd", Locale.getDefault()) // display the first character
        1 -> SimpleDateFormat("EEEE M/dd", Locale.getDefault()) // display full weekday
        else -> SimpleDateFormat("EEE M/dd", Locale.getDefault()) // display first three characters
    }
}

internal fun getDefaultTimeFormat(context: Context): SimpleDateFormat {
    val format = if (DateFormat.is24HourFormat(context)) "HH:mm" else "hh a"
    return SimpleDateFormat(format, Locale.getDefault())
}