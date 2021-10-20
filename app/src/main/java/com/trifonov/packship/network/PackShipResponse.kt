package com.trifonov.packship.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

sealed class PackShipResponse<out R> {
    data class Success<out T>(val data: T) : PackShipResponse<T>()
    data class Error(val exception: Exception, val message: String? = null) :
        PackShipResponse<Nothing>()
}

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): PackShipResponse<T> = withContext(Dispatchers.IO) {
    try {
        PackShipResponse.Success(apiCall.invoke())
    } catch (exception: Exception) {
        PackShipResponse.Error(exception, exception.message)
    }
}

val PackShipResponse<*>.succeeded
    get() = this is PackShipResponse.Success<*>

inline fun <T> PackShipResponse<T>.onSuccess(
    crossinline callback: (value: T) -> Unit
): PackShipResponse<T> {
    if (succeeded) {
        callback((this as PackShipResponse.Success<T>).data)
    }
    return this
}

inline fun <T> PackShipResponse<T>.onError(
    crossinline callback: (value: PackShipResponse.Error) -> Unit
): PackShipResponse<T> {
    if (!succeeded) {
        callback(this as PackShipResponse.Error)
    }
    return this
}

//fun <T> PackShipResponse<T>.isAuthorized(
//    authenticationChecker: AuthenticationChecker
//): PackShipResponse<T> {
//    if (!succeeded) {
//        val error = this as PackShipResponse.Error
//
//        if (error.exception is HttpException) {
//            if (error.exception.code() == Constants.HttpCodes.FORBIDDEN) {
//                authenticationChecker.setUnauthenticated()
//            }
//        }
//    }
//    return this
//}