package com.example.taskhub.utils

import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.*


class TimeManager{
    companion object{
        const val TAG = "DATEMANAGERSERVICE"

        fun getParsedDate(date: Long): List<String>{
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.time = Date(date)
            var month = (calendar.get(Calendar.MONTH) + 1).toString()

            if(month.length == 1)
                month = "0$month"

            return listOf(
                "${calendar.get(Calendar.DAY_OF_MONTH)}",
                month,
                "${calendar.get(Calendar.YEAR)}"
            )
        }

        fun getNowDate(): String {
            val sdf = SimpleDateFormat("d.MM.yyyy", Locale.getDefault())
            return sdf.format(Date())
        }

        fun getNowWeekDay(): String {
            val dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
            var out = DateFormatSymbols().weekdays[dayOfWeek]

            if (out.length == 3){
                out = when(out){
                    "Mon" -> "Monday"
                    "Tue" -> "Tuesday"
                    "Wed" -> "Wednesday"
                    "Thu" -> "Thursday"
                    "Fri" -> "Friday"
                    "Sat" -> "Saturday"
                    "Sun" -> "Sunday"
                    else -> out
                }
            }

            return out
        }

        /*
        fun getNowTime(): String{
            val date = Calendar.getInstance().time
            return SimpleDateFormat("h:mm", Locale.getDefault()).format(date.time)
        }*/

    }

}