package com.tinderlikeapp.data.network

sealed class APIResult<out T: Any> {
    data class Success<out T: Any>(val data: T) : APIResult<T>()
    data class Error(val apiError: APIError) : APIResult<Nothing>()
}