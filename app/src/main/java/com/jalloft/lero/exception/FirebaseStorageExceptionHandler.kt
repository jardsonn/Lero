package com.jalloft.lero.exception


import android.content.Context
import com.google.firebase.storage.StorageException
import com.jalloft.lero.R

class FirebaseStorageExceptionHandler(private val context: Context) {

    fun handleException(exception: StorageException): String {
        val errorCode = exception.errorCode
        val errorMessage = exception.message

        val resources = context.resources
        return when (errorCode) {
            StorageException.ERROR_BUCKET_NOT_FOUND -> resources.getString(R.string.error_storage_bucket_not_found)
            StorageException.ERROR_PROJECT_NOT_FOUND -> resources.getString(R.string.error_storage_project_not_found)
            StorageException.ERROR_QUOTA_EXCEEDED -> resources.getString(R.string.error_storage_quota_exceeded)
            StorageException.ERROR_NOT_AUTHENTICATED -> resources.getString(R.string.error_storage_not_authenticated)
            StorageException.ERROR_NOT_AUTHORIZED -> resources.getString(R.string.error_storage_not_authorized)
            StorageException.ERROR_RETRY_LIMIT_EXCEEDED -> resources.getString(R.string.error_storage_retry_limit_exceeded)
            StorageException.ERROR_INVALID_CHECKSUM -> resources.getString(R.string.error_storage_invalid_checksum)
            StorageException.ERROR_CANCELED -> resources.getString(R.string.error_storage_canceled)
            StorageException.ERROR_UNKNOWN -> resources.getString(R.string.error_storage_unknown)
            else -> String.format(resources.getString(R.string.error_storage_generic), errorCode, errorMessage)
        }
    }
}
