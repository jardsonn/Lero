package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class EducationLevel(
    @StringRes val nameResId: Int
) {
    MIDDLE_EDUCATION(R.string.middle_education),
    HIGHER_EDUCATION(R.string.higher_education),
    POST_GRADUATION(R.string.post_graduation),
    NON_INFORM(R.string.non_inform)
}