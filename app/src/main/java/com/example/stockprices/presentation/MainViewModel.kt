package com.example.stockprices.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockprices.domain.LoadBarListUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadBarListUseCase: LoadBarListUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Initial)
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.d("MainViewModel", "Exception caught: $throwable")
    }

    init{
        loadBarList()
    }

    private fun loadBarList() {
        viewModelScope.launch(exceptionHandler) {
            val barList=loadBarListUseCase.invoke()
            _state.value=MainState.Content(barList)
        }
    }
}