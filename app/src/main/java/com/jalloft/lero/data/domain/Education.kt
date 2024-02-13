package com.jalloft.lero.data.domain

import com.jalloft.lero.data.domain.enums.EducationLevel

/**
 * Created by Jardson Costa on 09/02/2024.
 */
data class Education(
    val level: EducationLevel? = null,
    val secondaryEducationInstitution: String? = null,
    val higherEducationInstitution: String? = null,
    val postGraduationInstitution: String? = null,
)
