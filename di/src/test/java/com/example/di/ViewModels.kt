package com.example.di

import androidx.lifecycle.ViewModel

class ViewModelA : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository
}

class ViewModelB : ViewModel() {
    @Inject
    lateinit var productRepository: ProductRepository
}

class ViewModelC : ViewModel() {
    @Inject
    @ExampleDatabaseRepository
    lateinit var cartRepositoryA: CartRepository

    @Inject
    @ExampleInMemoryRepository
    lateinit var cartRepositoryB: CartRepository
}
