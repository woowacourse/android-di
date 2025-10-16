package test.fixture

import com.example.di.DatabaseLogger
import com.example.di.InMemoryLogger

class FakeAppContainer {
    @InMemoryLogger
    val productRepository: FakeProductRepository by lazy {
        FakeProductRepositoryImpl()
    }

    @DatabaseLogger
    val productRepository2: FakeProductRepository by lazy {
        FakeProductRepositoryImpl()
    }

    @DatabaseLogger
    val cartRepository: FakeCartRepositoryImpl by lazy {
        FakeCartRepositoryImpl()
    }
}
