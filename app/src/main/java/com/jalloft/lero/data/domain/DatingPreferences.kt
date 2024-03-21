package com.jalloft.lero.data.domain

import com.jalloft.lero.data.domain.enums.Children
import com.jalloft.lero.data.domain.enums.Drinker
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.Smoker

/**
 * Created by Jardson Costa on 03/03/2024.
 */


data class DatingPreferences(
    val location: Choice<GeoLocalization>? = null,
    val distance: Choice<Int>? = Choice(200),
    val gender: Choice<List<SexualGender>>? = null,
    val ageRange: Choice<Range<Int>>? = null,
    val lookingFor: Choice<List<Interests>>? = null,
    val heightRange: Choice<Range<Height>>? = null,
    val children: Choice<Children>? = null,
    val smoker: Choice<Smoker>? = null,
    val drinker: Choice<Drinker>? = null,
)
