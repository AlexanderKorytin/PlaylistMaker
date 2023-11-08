package com.example.playlistmaker.search.domain.models

sealed interface SearchResultData<T> {
    data class Data<T>(val value: T) : SearchResultData<T>
    data class Error<T>(val message: String) : SearchResultData<T>
}