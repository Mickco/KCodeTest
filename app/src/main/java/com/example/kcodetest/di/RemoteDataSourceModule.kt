package com.example.kcodetest.di

import android.content.Context
import com.example.kcodetest.BuildConfig
import com.example.kcodetest.BuildConstant
import com.example.kcodetest.datasource.remote.interceptor.NetworkConnectionInterceptor
import com.example.kcodetest.datasource.remote.service.ITunesApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataSourceModule {

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(NetworkConnectionInterceptor(context))

        if (BuildConfig.DEBUG) {
            builder.addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }
        return builder.build()
    }


    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BuildConstant.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideITunesApiService(
        retrofit: Retrofit
    ): ITunesApiService {
        return retrofit.create(ITunesApiService::class.java)
    }


}