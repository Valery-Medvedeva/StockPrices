package com.example.stockprices.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stockprices.getMainComponent

class MainActivity : ComponentActivity() {
    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val component = getMainComponent()
            val viewModel: MainViewModel = viewModel(factory = component.getViewModelFactory())
            val screenState = viewModel.state.collectAsState()
            when (val currentState=screenState.value) {
                is MainScreenState.Initial -> {}
                is MainScreenState.Content -> {
                    MainScreen(bars = currentState.barList)
                }
            }
        }
    }
}
