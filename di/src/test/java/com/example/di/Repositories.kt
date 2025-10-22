package com.example.di

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier
annotation class ExampleDatabaseRepository

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY)
@Qualifier
annotation class ExampleInMemoryRepository

interface ProductRepository

class DefaultProductRepository : ProductRepository

interface CartRepository

class DatabaseCartRepository : CartRepository

class InMemoryCartRepository : CartRepository
