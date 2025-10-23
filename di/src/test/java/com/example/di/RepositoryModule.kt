package com.example.di

class RepositoryModule : Module {
    @Dependency
    @ViewModelLifespan
    fun productRepository(): ProductRepository = DefaultProductRepository()

    @Dependency
    @ExampleDatabaseRepository
    fun databaseCartRepository(): CartRepository = DatabaseCartRepository()

    @Dependency
    @ExampleInMemoryRepository
    fun inMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}
