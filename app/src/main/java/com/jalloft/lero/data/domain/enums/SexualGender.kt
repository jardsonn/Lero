package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class SexualGender(@StringRes val nameResId: Int) {
    MALE(R.string.gender_male),
    FEMALE(R.string.gender_female),
    NON_BINARY(R.string.gender_non_binary),
    GENDER_FLUID(R.string.gender_gender_fluid),
    TRANS_MALE(R.string.gender_trans_male),
    TRANS_FEMALE(R.string.gender_trans_female),
    OTHER(R.string.gender_other)
}