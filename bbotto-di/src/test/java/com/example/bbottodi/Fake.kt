package com.example.bbottodi

import com.example.bbottodi.di.Module
import com.example.bbottodi.di.annotation.InDisk
import com.example.bbottodi.di.annotation.InMemory
import com.example.bbottodi.di.annotation.Inject

class FakeViewModelWithInjectOnSuccess(
    @Inject
    val productRepository: FakeProductRepository,
)

class FakeViewModelWithInjectOnFailure(
    val productRepository: FakeProductRepository,
)

class FakeViewModelWithFieldInjectOnSuccess() {
    @Inject
    private lateinit var productRepository: FakeProductRepository
}

class FakeViewModelWithRecursiveInject(
    @Inject
    productRepository: FakeProductRepository,
    @Inject
    cartRepository: FakeInDiskCartRepository,
)

class FakeViewModelWithQualifier(
    @Inject
    productRepository: FakeProductRepository,
    @Inject
    @InDisk
    cartRepository: FakeCartRepository,
)

class FakeViewModelWithQualifierFieldInject(
    @Inject
    productRepository: FakeProductRepository,
) {
    @Inject
    @InDisk
    private lateinit var cartRepository: FakeCartRepository
}

class FakeViewModelWithInDiskAndInMemory(
    @Inject
    @InDisk
    cartInDiskRepository: FakeCartRepository,
    @Inject
    @InMemory
    cartInMemoryRepository: FakeCartRepository,
)

class FakeProductRepository
interface FakeCartRepository
class FakeDefaultCartRepository : FakeCartRepository
class FakeInMemoryCartRepository : FakeCartRepository
class FakeInDiskCartRepository(
    @Inject
    private val cartDao: FakeCartDao,
) : FakeCartRepository

class FakeCartDao
class FakeDateFormatter

class FakeApplicationModule : Module {
    fun provideCartDao(): FakeCartDao {
        return FakeCartDao()
    }
    fun provideDefaultCartRepository(): FakeCartRepository {
        return FakeDefaultCartRepository()
    }

    @InMemory
    fun provideInMemoryCartRepository(): FakeCartRepository {
        return FakeInMemoryCartRepository()
    }

    @InDisk
    fun provideInDiskCartRepository(fakeCartDao: FakeCartDao): FakeCartRepository {
        return FakeInDiskCartRepository(fakeCartDao)
    }
}

class FakeActivityModule : Module {
    fun provideDateFormatter(): FakeDateFormatter {
        return FakeDateFormatter()
    }
    fun provideProductRepository(): FakeProductRepository {
        return FakeProductRepository()
    }
}
