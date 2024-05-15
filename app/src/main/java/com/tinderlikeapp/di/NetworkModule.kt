package com.tinderlikeapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.tinderlikeapp.data.di.DefaultGson
import com.tinderlikeapp.data.di.ErrorDeserializerGson
import com.tinderlikeapp.data.network.APIError
import com.tinderlikeapp.data.network.ErrorDeserializer
import com.tinderlikeapp.data.network.service.RickyAndMortyApiService
import com.tinderlikeapp.data.network.service.call.APIResultCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val BASE_URL = "https://rickandmortyapi.com/api/"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpBuilder(): OkHttpClient.Builder {
        return OkHttpClient()
            .newBuilder().apply {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
                )
                connectTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
            }
    }

    @Singleton
    @Provides
    @DefaultGson
    fun provideGson(): Gson {
        return Gson()
    }

    @Singleton
    @Provides
    @ErrorDeserializerGson
    fun provideErrorDeserializerGson(): Gson {
        return GsonBuilder()
            .registerTypeAdapter(APIError::class.java, ErrorDeserializer())
            .create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(
        @DefaultGson gson: Gson
    ): GsonConverterFactory {
        return GsonConverterFactory.create(gson)
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient.Builder,
        gsonConverterFactory: GsonConverterFactory,
        @ErrorDeserializerGson gson: Gson,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(gsonConverterFactory)
            .addCallAdapterFactory(APIResultCallAdapterFactory.create(gson))
            .build()
    }

    @Provides
    fun provideApiService(
        retrofit: Retrofit
    ): RickyAndMortyApiService {
        return retrofit.create(RickyAndMortyApiService::class.java)
    }

}