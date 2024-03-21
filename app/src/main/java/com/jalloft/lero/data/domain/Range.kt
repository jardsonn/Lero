package com.jalloft.lero.data.domain

/**
 * Created by Jardson Costa on 03/03/2024.
 */
data class Range<T>(
    val min: T? = null,
    val max: T? = null,
){
    override fun toString(): String {
        return "$min - $max"
    }
}
