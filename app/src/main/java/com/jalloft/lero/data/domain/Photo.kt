package com.jalloft.lero.data.domain

import com.google.firebase.Timestamp

/**
 * Created by Jardson Costa on 09/02/2024.
 */
data class Photo(
    val url: String? = null,
    val userCreatedIn: Timestamp? = null,
)
