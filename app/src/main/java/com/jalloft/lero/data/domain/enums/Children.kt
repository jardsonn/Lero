package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class Children(@StringRes val nameResId: Int) {
    YES(R.string.yes_i_have_children),
    NO(R.string.no_i_dont_have_children),
    NON_INFORM(R.string.non_inform)
}
