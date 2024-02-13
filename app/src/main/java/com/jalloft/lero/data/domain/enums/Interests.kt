package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R


enum class Interests(
    @StringRes
    val nameResId: Int
) {
    CHAT_CHAT(R.string.chat), FRIENDSHIP(R.string.friendship), SOMETHING_CASUAL(R.string.something_casual), LONG_RELATIONSHIP(
        R.string.long_relationship
    )
}

