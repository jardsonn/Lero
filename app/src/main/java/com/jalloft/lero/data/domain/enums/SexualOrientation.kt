package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class  SexualOrientation(@StringRes val nameResId: Int) {
    STRAIGHT(R.string.orientation_straight),
    GAY(R.string.orientation_gay),
    LESBIAN(R.string.orientation_lesbian),
    BISEXUAL(R.string.orientation_bisexual),
    PANSEXUAL(R.string.orientation_pansexual),
    ASEXUAL(R.string.orientation_asexual),
    QUEER(R.string.orientation_queer),
    QUESTIONING(R.string.orientation_questioning),
    DEMISEXUAL(R.string.orientation_demisexual),
    GREYSEXUAL(R.string.orientation_greysexual),
    OTHER(R.string.orientation_other)
}