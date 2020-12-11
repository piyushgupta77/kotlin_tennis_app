package com.kotlin.tennisapplication.di

import com.kotlin.tennisapplication.model.actions.PlayerActionGenerator
import com.kotlin.tennisapplication.model.actions.PlayerActionProcessor
import com.kotlin.tennisapplication.model.points.PointsProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class HiltModule {
    @Provides
    @Singleton
    fun providePlayerActionGenerator(): PlayerActionGenerator {
        return PlayerActionGenerator()
    }

    @Provides
    @Singleton
    fun providePlayerActionProcessor(processor: PointsProcessor): PlayerActionProcessor {
        return PlayerActionProcessor(processor)
    }

    @Provides
    @Singleton
    fun providePointsProcessor(): PointsProcessor {
        return PointsProcessor()
    }
}