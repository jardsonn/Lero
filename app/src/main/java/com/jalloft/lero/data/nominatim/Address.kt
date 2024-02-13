package com.jalloft.lero.data.nominatim


import com.google.gson.annotations.SerializedName

data class Address(
    @SerializedName("city")
    val city: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("country_code")
    val countryCode: String?,
    @SerializedName("ISO3166-2-lvl4")
    val iSO31662Lvl4: String?,
    @SerializedName("state")
    val state: String?
)