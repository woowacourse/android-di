package com.example.di

interface ViewModelScoped {
    var diScope: ViewModelContainer?

    fun closeScope() {
        diScope?.clear()
        diScope = null
    }
}
