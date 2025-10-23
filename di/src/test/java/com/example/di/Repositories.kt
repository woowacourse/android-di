package com.example.di

@Qualifier
annotation class ExampleDatabaseRepository

@Qualifier
annotation class ExampleInMemoryRepository

interface ProductRepository

class DefaultProductRepository : ProductRepository

interface CartRepository

class DatabaseCartRepository : CartRepository

class InMemoryCartRepository : CartRepository
