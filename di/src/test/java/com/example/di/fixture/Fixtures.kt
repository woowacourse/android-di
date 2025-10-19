package com.example.di.fixture

import com.example.di.Inject
import com.example.di.Qualifier

// Qualifiers
@Qualifier
annotation class InMemory

@Qualifier
annotation class LocalDatabase

// Repositories
interface ProductRepository
class DefaultProductRepository : ProductRepository

interface CartRepository
class DefaultCartRepository : CartRepository
class FakeCartRepository : CartRepository

interface UserRepository
class CartViewModel @Inject constructor(val cartRepository: CartRepository)

class CartUseCase @Inject constructor(
    @InMemory val fakeCartRepository: CartRepository,
    @LocalDatabase val realCartRepository: CartRepository,
)

class MainViewModel @Inject constructor() {
    @field: Inject
    lateinit var productRepository: ProductRepository

    @field: Inject
    lateinit var cartRepository: CartRepository
}
