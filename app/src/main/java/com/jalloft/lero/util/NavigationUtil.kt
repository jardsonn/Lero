package com.jalloft.lero.util

import com.jalloft.lero.data.domain.User
import com.jalloft.lero.data.domain.enums.Children
import com.jalloft.lero.data.domain.enums.Drinker
import com.jalloft.lero.data.domain.enums.Religion
import com.jalloft.lero.data.domain.enums.Smoker
import com.jalloft.lero.ui.navigation.GraphDestination
import com.jalloft.lero.ui.navigation.RegisterDataDestination


const val CURRENTE_REGISTRATION_ROUTE_KEY = "currente_registration_route_key"
const val MANDATORY_DATA_SAVED = "mandatory_data_saved"

fun getRoute(user: User?): String{
    if (user != null) {
        if (user.name == null || user.dateOfBirth == null || user.height == null) {
            return RegisterDataDestination.EssentialInformation.route 
        } else if (user.gender == null || user.orientation == null) {
            return RegisterDataDestination.SexualIdentification.route 
        } else if (user.city == null) {
            return RegisterDataDestination.Birthplace.route 
        } else if (user.interests.isEmpty()) {
            return RegisterDataDestination.Interest.route 
        } else if (user.work?.profission == null || user.work.company == null || user.education?.level == null || user.education.higherEducationInstitution == null || user.education.secondaryEducationInstitution == null || user.education.postGraduationInstitution == null) {
            return RegisterDataDestination.WorkAndEducation.route 
        } else if (user.children == Children.NON_INFORM || user.smoker == Smoker.NON_INFORM || user.drinker == Drinker.NON_INFORM || user.religion == Religion.NON_INFORM) {
            return RegisterDataDestination.Lifestyle.route 
        } else if (user.hobbies.isEmpty()) {
            return RegisterDataDestination.Hobbies.route 
        } else if (user.bio == null) {
            return RegisterDataDestination.Bio.route 
        } else if (user.photos.isNullOrEmpty() || user.profilePhoto == null) {
            return RegisterDataDestination.Photo.route
        } else {
            return GraphDestination.LoggedIn.route 
        }
    } else {
        return RegisterDataDestination.EssentialInformation.route 
    }
}
