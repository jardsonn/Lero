package com.jalloft.lero.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Jardson Costa on 10/02/2024.
 */
object DateUtil {

    const val INVALID_DATE_FORMT = 0
    const val INVALID_DATE_MINOR = 1
    const val VALID_DATE = 10

    private val countryDateFormats = mapOf(
        "BR" to "ddMMyyyy",
        "US" to "MMddyyyy",
        "CN" to "yyyyMMdd",
    )

    fun dateToString(date: Date?): String? {
        val countryCode = Locale.getDefault().country
        val dateFormatPattern = countryDateFormats[countryCode] ?: "ddMMyyyy"
        val dateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())
        return date?.let { dateFormat.format(it) }
    }

    fun stringToDate(dateString: String?): Date? {
        if (dateString == null) return null
        val countryCode = Locale.getDefault().country
        val dateFormatPattern = countryDateFormats[countryCode] ?: "ddMMyyyy"
        val dateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())
        return dateFormat.parse(dateString)
    }

    fun stringToTimestamp(dateString: String?): Timestamp? {
        return stringToDate(dateString)?.let { Timestamp((it)) }
    }

    fun isBirthDateValid(dateString: String?): Int {
        if (dateString == null) return INVALID_DATE_FORMT

        val countryCode = Locale.getDefault().country
        val dateFormatPattern = countryDateFormats[countryCode] ?: "ddMMyyyy"
        val dateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())

        dateFormat.isLenient = false

        return try {
            val date = dateFormat.parse(dateString)
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }

            // Checking if year, month, and day are within correct limits
            val validYear = calendar.get(Calendar.YEAR) in 1900..9999
            val validMonth = calendar.get(Calendar.MONTH) in Calendar.JANUARY..Calendar.DECEMBER
            val validDay =
                calendar.get(Calendar.DAY_OF_MONTH) in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

            val minimumAge = 18
            val today = Calendar.getInstance()
            today.add(Calendar.YEAR, -minimumAge) // subtract 18 years from the current date

            // Checks if the birth date is before today, ensuring the person is over 18 years old
            val isOver18 = calendar.before(today)

            if (validYear && validMonth && validDay) {
                if (isOver18) {
                    VALID_DATE
                } else {
                    INVALID_DATE_MINOR
                }
            } else {
                INVALID_DATE_FORMT
            }
        } catch (e: Exception) {
            INVALID_DATE_FORMT
        }
    }

    fun calculateAge(dateOfBirth: Date?): Int {
        val currentDate = Calendar.getInstance()
        val dob = Calendar.getInstance()
        if (dateOfBirth != null) {
            dob.time = dateOfBirth
        }

        var age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (currentDate.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        return age
    }

}