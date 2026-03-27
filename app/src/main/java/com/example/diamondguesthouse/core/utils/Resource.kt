package com.example.diamondguesthouse.core.utils

sealed interface Resource<out T> {
    data object Loading : Resource<Nothing>
    data object Empty : Resource<Nothing>
    data class Success<T>(val value: T) : Resource<T>
}
