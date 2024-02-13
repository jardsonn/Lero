package com.jalloft.lero.util

import android.content.Context
import android.telephony.TelephonyManager
import com.jalloft.lero.data.domain.enums.CountryPhoneNumber


class CountryCodeDetector(private val context: Context) {

    fun getCountryCode(): CountryPhoneNumber {
        val telephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.networkCountryIso
        return CountryPhoneNumber.entries.find { it.countryCode == countryCode?.uppercase() }
            ?: CountryPhoneNumber.UNITED_STATES
    }
}