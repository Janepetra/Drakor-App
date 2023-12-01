package com.jane.drakorapp.di

import com.jane.drakorapp.data.DrakorRepository
import com.jane.drakorapp.data.Repository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun provideDrakorRepository(repository: DrakorRepository): Repository
}