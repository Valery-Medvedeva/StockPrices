package com.example.stockprices.di

import com.example.stockprices.data.RepositoryImpl
import com.example.stockprices.data.network.ApiFactory
import com.example.stockprices.data.network.ApiService
import com.example.stockprices.domain.Repository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class DataModule {

    @AppScope
    @Binds
    abstract fun bindRepository(impl: RepositoryImpl):Repository

    companion object{
        @AppScope
        @Provides
        fun provideApiService():ApiService{
            return ApiFactory.apiService
        }
    }

}