package com.amber.mvi.view

import androidx.lifecycle.*
import com.amber.mvi.UserRepository
import com.amber.mvi.intent.MainIntent
import com.amber.mvi.intent.MainState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableLiveData<MainState>(MainState.Idle)
    val state: LiveData<MainState> get() = _state
    private val userIntent = Channel<MainIntent>(Channel.UNLIMITED)

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchUser -> fecthUser()
                }
            }
        }
    }

    fun fecthUser() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Users(repository.getUsers().results)
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

    fun onClickFetch() {
        viewModelScope.launch {
            userIntent.send(MainIntent.FetchUser)
        }
    }
}

class MainViewModelFactory(val repository: UserRepository) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MainViewModel::class.java))
            MainViewModel(repository) as T
        else super.create(modelClass)
    }
}