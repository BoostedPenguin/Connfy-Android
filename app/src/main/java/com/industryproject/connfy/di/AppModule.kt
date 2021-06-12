package com.industryproject.connfy.di

import com.google.gson.GsonBuilder
import com.industryproject.connfy.BuildConfig
import com.industryproject.connfy.api.*
import com.industryproject.connfy.api.meetingsApi.MeetingHelper
import com.industryproject.connfy.api.meetingsApi.MeetingHelperImpl
import com.industryproject.connfy.api.meetingsApi.MeetingService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule{

    @Provides
    fun provideBaseUrl() = "http://10.0.2.2:3000/"


    class NullOnEmptyConverterFactory : Converter.Factory() {
        override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *> {
            val delegate: Converter<ResponseBody, *> = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter<ResponseBody, Any?> { body ->


                val contentLength = body.contentLength()
                if (contentLength == 0L) {
                    null
                } else delegate.convert(body)

//                val contentBody = body.string()
//                if (contentBody == "{[]}") { // Or whatever the empty array is returned as
//                    null
//                } else delegate.convert(body)

            }
        }
    }


    @Singleton
    @Provides
    fun provideOkHttpClient() = if (BuildConfig.DEBUG){
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }else{
        OkHttpClient
            .Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL:String): Retrofit = Retrofit.Builder()
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit) = retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideUserHelper(apiHelper: UserHelperImpl): UserHelper = apiHelper

    @Provides
    @Singleton
    fun provideMeetingService(retrofit: Retrofit) = retrofit.create(MeetingService::class.java)

    @Provides
    @Singleton
    fun provideMeetingHelper(apiHelper: MeetingHelperImpl): MeetingHelper = apiHelper

}