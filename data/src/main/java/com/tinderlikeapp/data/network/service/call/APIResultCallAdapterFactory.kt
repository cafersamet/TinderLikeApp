package com.tinderlikeapp.data.network.service.call

import com.google.gson.Gson
import com.tinderlikeapp.data.network.APIResult
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class APIResultCallAdapterFactory(
    private val gson: Gson
): CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        val callRawType = getRawType(returnType)
        if (callRawType != Call::class.java) {
            return null
        }
        val apiResultType = getParameterUpperBound(0, returnType as ParameterizedType)
        val apiResultRawType = getRawType(apiResultType)
        if (apiResultRawType != APIResult::class.java) {
            return null
        }
        val resultType = getParameterUpperBound(0, apiResultType as ParameterizedType)
        return APIResultCallAdapter(resultType, gson)
    }

    companion object {
        fun create(gson: Gson): APIResultCallAdapterFactory {
            return APIResultCallAdapterFactory(gson)
        }
    }
}