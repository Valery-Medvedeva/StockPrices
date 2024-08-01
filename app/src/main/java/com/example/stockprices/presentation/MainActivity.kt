package com.example.stockprices.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
                is MainState.Initial -> {}
                is MainState.Content -> {
                    MainScreen(bars = currentState.barList)
                }
            }
        }
    }
}
