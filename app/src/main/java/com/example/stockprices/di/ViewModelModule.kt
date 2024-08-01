package com.example.stockprices.di

import androidx.lifecycle.ViewModel
import com.example.stockprices.presentation.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindsMainViewModel(impl:MainViewModel):ViewModel
}