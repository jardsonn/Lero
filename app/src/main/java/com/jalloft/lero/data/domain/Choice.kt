package com.jalloft.lero.data.domain


data class Choice<T>(
    val preference: T? = null,
    val isStrong: Boolean? = false
)