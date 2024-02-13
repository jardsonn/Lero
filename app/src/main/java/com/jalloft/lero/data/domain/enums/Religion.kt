package com.jalloft.lero.data.domain.enums

import androidx.annotation.StringRes
import com.jalloft.lero.R

/**
 * Created by Jardson Costa on 12/02/2024.
 */


enum class Religion(val nameResId: Int) {
    NON_INFORM(R.string.non_inform),
    CHRISTIANITY(R.string.religion_christianity),
    ISLAM(R.string.religion_islam),
    HINDUISM(R.string.religion_hinduism),
    BUDDHISM(R.string.religion_buddhism),
    JUDAISM(R.string.religion_judaism),
    SIKHISM(R.string.religion_sikhism),
    SHINTO(R.string.religion_shinto),
    BAHAI(R.string.religion_bahai),
    JAINISM(R.string.religion_jainism),
    TAOISM(R.string.religion_taoism),
    ZOROASTRIANISM(R.string.religion_zoroastrianism),
    CONFUCIANISM(R.string.religion_confucianism),
    WICCA(R.string.religion_wicca),
    NEO_PAGANISM(R.string.religion_neo_paganism),
    ATHEISM(R.string.religion_atheism),
    AGNOSTICISM(R.string.religion_agnosticism),
    RASTAFARIANISM(R.string.religion_rastafarianism),
    DRUIDR(R.string.religion_druidry),
    CAODAISM(R.string.religion_caodaism),
    TENRIKYO(R.string.religion_tenrikyo),
    UNITARIAN_UNIVERSALISM(R.string.religion_unitarian_universalism),
    SEICHO_NO_IE(R.string.religion_seicho_no_ie),
    ECKANKAR(R.string.religion_eckankar),
    SCIENTOLOGY(R.string.religion_scientology),
    NEW_THOUGHT(R.string.religion_new_thought),
    HUMANISM(R.string.religion_humanism),
    WICCAN_RECONSTRUCTIONISM(R.string.religion_wiccan_reconstructionism),
    DEISM(R.string.religion_deism),
    SHAMANISM(R.string.religion_shamanism),
    OTHER(R.string.religion_other)
}
