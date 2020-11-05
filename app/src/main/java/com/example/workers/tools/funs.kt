package com.example.workers.tools

import java.util.*

fun ageCalc(birthday: String?) : String {
    if (birthday.isNullOrEmpty()) {
        return "-"
    }

    val year = Calendar.getInstance().get(Calendar.YEAR)
    val month = Calendar.getInstance().get(Calendar.MONTH)
    val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    val birthdayArray = birthday.split('.')

    if (birthdayArray[1].toInt() > month) {
        return (year - birthdayArray[2].toInt()).toString()
    }
    else if (birthdayArray[1].toInt() == month && birthdayArray[0].toInt() >= day) {
        return (year - birthdayArray[2].toInt()).toString()
    }
    else {
        return (year - birthdayArray[2].toInt() - 1).toString()
    }
}