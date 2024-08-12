package com.example.stockprices.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockprices.domain.LoadBarListUseCase
import com.example.stockprices.domain.TimeFrame
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val loadBarListUseCase: LoadBarListUseCase,
) : ViewModel() {

    private var lastState: MainScreenState = MainScreenState.Initial

    private val _state = MutableStateFlow<MainScreenState>(MainScreenState.Initial)
    val state = _state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        lastState.let {
            when (it) {
                is MainScreenState.Content -> {
                    _state.value = it.copy(sideEffect = Event.Error)
                }

                is MainScreenState.Initial -> {
                    _state.value = MainScreenState.Error
                }

                else -> {
                    _state.value = it
                }
            }
        }
    }

    init {
        loadBarList()
    }

    fun loadBarList(timeFrame: TimeFrame = TimeFrame.HOUR_1) {
        lastState = _state.value
        _state.value = MainScreenState.Loading
        viewModelScope.launch(exceptionHandler) {
            val barList = loadBarListUseCase.loadBars(timeFrame)
            _state.value = MainScreenState.Content(barList, timeFrame)
        }
    }
}