package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class Drinker(@StringRes val nameResId: Int) {
    YES_OFTEN(R.string.yes_often),
    YES_SOMETIMES(R.string.yes_sometimes),
    NO_NEVER(R.string.no_never),
    NON_INFORM(R.string.non_inform)
}
