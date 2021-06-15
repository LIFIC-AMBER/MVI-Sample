package com.amber.mvi.intent

sealed class MainIntent {
    object FetchUser : MainIntent()
}
