package com.jalloft.lero.data.nominatim


import com.google.gson.annotations.SerializedName
import com.jalloft.lero.data.domain.City

data class Place(
    @SerializedName("place_id") val placeId: Long? = null,
    @SerializedName("licence") val licence: String? = null,
    @SerializedName("osm_type") val osmType: String? = null,
    @SerializedName("address") val address: Address?,
    @SerializedName("osm_id") val osmId: Long? = null,
    @SerializedName("lat") val latitude: String? = null,
    @SerializedName("lon") val longitude: String? = null,
    @SerializedName("class") val classType: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("place_rank") val placeRank: Int? = null,
    @SerializedName("importance") val importance: Double? = null,
    @SerializedName("addresstype") val addressType: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("display_name") val displayName: String? = null,
    @SerializedName("boundingbox") val boundingBox: List<String> = listOf()
){

    val toCity: City
        get() = City(
            name,
            placeId,
            osmId,
            latitude,
            longitude,
            address?.state,
            address?.countryCode,
            address?.country
        )

}

//private val Place.toCity: City?
//    get() {}


