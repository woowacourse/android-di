package com.example.di

sealed class Component {
    data object Singleton : Component()

    data class Activity(val owner: Any) : Component()

    data class ViewModel(val owner: Any) : Component()
}
