package com.tinderlikeapp.data.network

import com.tinderlikeapp.data.network.APIErrorCode.UNKNOWN

data class APIError(
    val code : Int = UNKNOWN,
    val message : String? = null
)