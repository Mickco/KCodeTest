package com.example.kcodetest.di

import com.example.kcodetest.repository.model.KResult
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class BroadcastModule {

    @Singleton
    @Provides
    @Named("errorDisplayRequest")
    fun provideErrorDisplayRequest(): MutableSharedFlow<KResult.Fail> {
        return MutableSharedFlow()
    }
}