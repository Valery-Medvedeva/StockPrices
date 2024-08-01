package com.example.stockprices

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.stockprices.di.DaggerMainComponent
import com.example.stockprices.di.MainComponent

class App : Application() {

    val component by lazy {
        DaggerMainComponent.factory().create(this)
    }
}

@Composable
fun getMainComponent(): MainComponent {
    return (LocalContext.current.applicationContext as App).component
}