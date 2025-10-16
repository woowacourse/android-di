package com.example.di

class RepositoryModule : Module {
    @Dependency
    val productRepository: ProductRepository = DefaultProductRepository()

    @Dependency
    @DatabaseRepository
    val databaseCartRepository: CartRepository = DatabaseCartRepository()

    @Dependency
    @InMemoryRepository
    val inMemoryCartRepository: CartRepository = InMemoryCartRepository()
}
