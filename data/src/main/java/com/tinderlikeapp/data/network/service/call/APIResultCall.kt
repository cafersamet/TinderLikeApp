package com.tinderlikeapp.data.network.service.call

import com.google.gson.Gson
import com.tinderlikeapp.data.network.APIError
import com.tinderlikeapp.data.network.APIErrorCode.UNKNOWN
import com.tinderlikeapp.data.network.APIResult
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class APIResultCall<T : Any>(
    private val delegate: Call<T>,
    private val gson: Gson
) : Call<APIResult<T>> {

    override fun enqueue(callback: Callback<APIResult<T>>) {
        return delegate.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(
                            this@APIResultCall,
                            Response.success(APIResult.Success(body))
                        )
                    } else {
                        val code = response.code()
                        callback.onResponse(
                            this@APIResultCall,
                            Response.success(
                                APIResult.Error(APIError(code, null))
                            )
                        )
                    }
                } else {
                    val errorBody = response.errorBody()
                    if (errorBody != null) {
                        val apiError = gson.fromJson(errorBody.toString(), APIError::class.java)
                        callback.onResponse(
                            this@APIResultCall,
                            Response.success(APIResult.Error(apiError))
                        )
                    } else {
                        val apiError = APIError()
                        callback.onResponse(
                            this@APIResultCall,
                            Response.success(APIResult.Error(apiError))
                        )
                    }
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val apiError = when (t) {
                    is IOException -> APIError(
                        code = UNKNOWN,
                        message = t.message
                    )
                    else -> APIError()
                }
                callback.onResponse(
                    this@APIResultCall,
                    Response.success(APIResult.Error(apiError))
                )
            }
        })
    }

    override fun clone() = APIResultCall(delegate.clone(), gson)

    override fun execute() = throw UnsupportedOperationException()

    override fun isExecuted() = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()

}