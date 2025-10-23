package com.example.di

import androidx.lifecycle.ViewModel

class ViewModelA(
    val productRepository: ProductRepository,
) : ViewModel()

class ViewModelB(
    val productRepository: ProductRepository,
) : ViewModel()

class ViewModelC(
    @ExampleDatabaseRepository val cartRepositoryA: CartRepository,
    @ExampleInMemoryRepository val cartRepositoryB: CartRepository,
) : ViewModel()
