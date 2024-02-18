package com.jalloft.lero.exception


import android.content.Context
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.FirebaseFirestoreException.Code
import com.jalloft.lero.R

class FirebaseFirestoreExceptionHandler(private val context: Context) {

    fun handleException(exception: FirebaseFirestoreException): String {
        val errorCode = exception.code
        val errorMessage = exception.message

        val resources = context.resources
        return when (errorCode) {
            Code.OK -> resources.getString(R.string.error_firestore_ok)
            Code.CANCELLED -> resources.getString(R.string.error_firestore_cancelled)
            Code.UNKNOWN -> resources.getString(R.string.error_firestore_unknown)
            Code.INVALID_ARGUMENT -> resources.getString(R.string.error_firestore_invalid_argument)
            Code.DEADLINE_EXCEEDED -> resources.getString(R.string.error_firestore_deadline_exceeded)
            Code.NOT_FOUND -> resources.getString(R.string.error_firestore_not_found)
            Code.ALREADY_EXISTS -> resources.getString(R.string.error_firestore_already_exists)
            Code.PERMISSION_DENIED -> resources.getString(R.string.error_firestore_permission_denied)
            Code.UNAUTHENTICATED -> resources.getString(R.string.error_firestore_unauthenticated)
            Code.RESOURCE_EXHAUSTED -> resources.getString(R.string.error_firestore_resource_exhausted)
            Code.FAILED_PRECONDITION -> resources.getString(R.string.error_firestore_failed_precondition)
            Code.ABORTED -> resources.getString(R.string.error_firestore_aborted)
            Code.OUT_OF_RANGE -> resources.getString(R.string.error_firestore_out_of_range)
            Code.UNIMPLEMENTED -> resources.getString(R.string.error_firestore_unimplemented)
            Code.INTERNAL -> resources.getString(R.string.error_firestore_internal)
            Code.UNAVAILABLE -> resources.getString(R.string.error_firestore_unavailable)
            Code.DATA_LOSS -> resources.getString(R.string.error_firestore_data_loss)
            else -> String.format(resources.getString(R.string.error_firestore_generic), errorCode, errorMessage)
        }
    }
}
