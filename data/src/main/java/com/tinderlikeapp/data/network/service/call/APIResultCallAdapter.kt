package com.tinderlikeapp.data.network.service.call

import com.google.gson.Gson
import com.tinderlikeapp.data.network.APIResult
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class APIResultCallAdapter(
    private val responseType: Type,
    private val gson: Gson
): CallAdapter<Type, Call<APIResult<Type>>> {
    override fun responseType() = responseType

    override fun adapt(call: Call<Type>) = APIResultCall(call, gson)
}