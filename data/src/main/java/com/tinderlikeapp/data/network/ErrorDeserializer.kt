package com.tinderlikeapp.data.network

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

class ErrorDeserializer: JsonDeserializer<APIError> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): APIError {
        val jsonObject = json?.asJsonObject
        val jsonError = jsonObject?.get("error")?.asJsonObject
        return Gson().fromJson(jsonError.toString(), APIError::class.java)
    }
}