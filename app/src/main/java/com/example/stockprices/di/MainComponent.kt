package com.example.stockprices.di

import android.content.Context
import com.example.stockprices.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@AppScope
@Component(modules = [DataModule::class, ViewModelModule::class])
interface MainComponent {

    fun inject(activity: MainActivity)

    fun getViewModelFactory():ViewModelFactory

    @Component.Factory
    interface MainComponentFactory{
        fun create (@BindsInstance context: Context): MainComponent
    }
}