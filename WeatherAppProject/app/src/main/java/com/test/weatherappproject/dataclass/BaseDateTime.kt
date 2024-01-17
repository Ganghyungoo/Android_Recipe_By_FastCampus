package com.test.weatherappproject.dataclass

import java.time.LocalDateTime
import java.time.LocalTime

class BaseDateTime {
    var baseDate: String = ""
    var baseTime: String = ""


    fun setBaseDateTime() {
        var dateTime = LocalDateTime.now()
        val myBaseTime = when (dateTime.toLocalTime()) {
            in LocalTime.of(0, 0)..LocalTime.of(2, 30) -> {
                dateTime = dateTime.minusDays(1)
                "2300"
            }

            in LocalTime.of(2, 30)..LocalTime.of(5, 30) -> "0200"
            in LocalTime.of(5, 30)..LocalTime.of(8, 30) -> "0500"
            in LocalTime.of(8, 30)..LocalTime.of(11, 30) -> "0800"
            in LocalTime.of(11, 30)..LocalTime.of(14, 30) -> "1100"
            in LocalTime.of(14, 30)..LocalTime.of(17, 30) -> "1400"
            in LocalTime.of(17, 30)..LocalTime.of(20, 30) -> "1700"
            in LocalTime.of(20, 30)..LocalTime.of(23, 30) -> "2000"
            else -> "0200"
        }
        val myBaseDate =
            String.format("%04d%02d%02d", dateTime.year, dateTime.monthValue, dateTime.dayOfMonth)

        //멤버 설정
        this.baseTime = myBaseTime
        this.baseDate = myBaseDate
    }
}