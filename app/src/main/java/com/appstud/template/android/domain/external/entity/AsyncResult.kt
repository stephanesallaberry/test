package com.appstud.template.android.domain.external.entity

sealed class AsyncResult<T> {
    data class Success<T>(val data: T) : AsyncResult<T>() // Status success and data of the result
    data class Fail<T>(val exception: Exception) : AsyncResult<T>() // Status Error an error message

    // string method to display a result for debugging
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Fail -> "Error[exception=$exception]"
        }
    }
}
