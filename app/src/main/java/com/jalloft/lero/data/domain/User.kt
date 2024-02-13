package com.jalloft.lero.data.domain

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import com.jalloft.lero.data.domain.enums.Children
import com.jalloft.lero.data.domain.enums.Drinker
import com.jalloft.lero.data.domain.enums.Interests
import com.jalloft.lero.data.domain.enums.Religion
import com.jalloft.lero.data.domain.enums.SexualGender
import com.jalloft.lero.data.domain.enums.SexualOrientation
import com.jalloft.lero.data.domain.enums.Smoker

data class User(
    val name: String? = null,
    val dateOfBirth: Timestamp? = null,
    val bio: String? = null,
    val phoneNumber: String? = null,
    val email: String? = null,
    val height: Height? = null,
    val gender: SexualGender? = null,
    val orientation: SexualOrientation? = null,
    val education: Education? = null,
    val location: String? = null,
    val religionId: Religion = Religion.NON_INFORM,
    val city: City? = null,
    val work: Work? = null,
    val drinker: Drinker = Drinker.NON_INFORM,
    val smoker: Smoker = Smoker.NON_INFORM,
    val children: Children = Children.NON_INFORM,
    val interests: List<Interests> = listOf(),
    val photos: List<Photo> = listOf(),
    val profilePhoto: Photo? = null,
    val hobbies: List<String> = listOf(),
    val userCreatedIn: FieldValue? = null,
    val isVerified: Boolean = false,
)
