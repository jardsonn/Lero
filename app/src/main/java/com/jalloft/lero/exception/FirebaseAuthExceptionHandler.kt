package com.jalloft.lero.exception


import android.content.Context
import com.google.firebase.auth.FirebaseAuthException
import com.jalloft.lero.R

class FirebaseAuthExceptionHandler(private val context: Context) {

    fun handleException(exception: FirebaseAuthException): String {
        val errorCode = exception.errorCode
        val errorMessage = exception.message

        val resources = context.resources
        return when (errorCode) {
            "ERROR_INVALID_CUSTOM_TOKEN" -> resources.getString(R.string.error_invalid_custom_token)
            "ERROR_CUSTOM_TOKEN_MISMATCH" -> resources.getString(R.string.error_custom_token_mismatch)
            "ERROR_INVALID_CREDENTIAL" -> resources.getString(R.string.error_invalid_credential)
            "ERROR_INVALID_EMAIL" -> resources.getString(R.string.error_invalid_email)
            "ERROR_WRONG_PASSWORD" -> resources.getString(R.string.error_wrong_password)
            "ERROR_USER_MISMATCH" -> resources.getString(R.string.error_user_mismatch)
            "ERROR_REQUIRES_RECENT_LOGIN" -> resources.getString(R.string.error_requires_recent_login)
            "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> resources.getString(R.string.error_account_exists_with_different_credential)
            "ERROR_EMAIL_ALREADY_IN_USE" -> resources.getString(R.string.error_email_already_in_use)
            "ERROR_CREDENTIAL_ALREADY_IN_USE" -> resources.getString(R.string.error_credential_already_in_use)
            "ERROR_USER_DISABLED" -> resources.getString(R.string.error_user_disabled)
            "ERROR_USER_TOKEN_EXPIRED" -> resources.getString(R.string.error_user_token_expired)
            "ERROR_USER_NOT_FOUND" -> resources.getString(R.string.error_user_not_found)
            "ERROR_INVALID_USER_TOKEN" -> resources.getString(R.string.error_invalid_user_token)
            "ERROR_OPERATION_NOT_ALLOWED" -> resources.getString(R.string.error_operation_not_allowed)
            "ERROR_WEAK_PASSWORD" -> resources.getString(R.string.error_weak_password)
            "ERROR_MISSING_EMAIL" -> resources.getString(R.string.error_missing_email)
            "ERROR_MISSING_PHONE_NUMBER" -> resources.getString(R.string.error_missing_phone_number)
            "ERROR_INVALID_PHONE_NUMBER" -> resources.getString(R.string.error_invalid_phone_number)
            "ERROR_MISSING_VERIFICATION_CODE" -> resources.getString(R.string.error_missing_verification_code)
            "ERROR_INVALID_VERIFICATION_CODE" -> resources.getString(R.string.error_invalid_verification_code)
            "ERROR_SESSION_EXPIRED" -> resources.getString(R.string.error_session_expired)
            else -> String.format(resources.getString(R.string.error_generic), errorCode, errorMessage)
        }
    }
}
