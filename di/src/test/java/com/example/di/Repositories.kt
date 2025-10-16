package com.example.di

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class DatabaseRepository

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Qualifier
annotation class InMemoryRepository

interface ProductRepository

class DefaultProductRepository : ProductRepository

interface CartRepository

class DatabaseCartRepository : CartRepository

class InMemoryCartRepository : CartRepository
