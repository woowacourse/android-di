package test.fixture

import com.example.di.DatabaseLogger
import com.example.di.InMemoryLogger

class FakeAppContainer {
    @InMemoryLogger
    val productRepository: FakeProductRepositoryImpl by lazy {
        FakeProductRepositoryImpl()
    }

    @DatabaseLogger
    val cartRepository: FakeCartRepositoryImpl by lazy {
        FakeCartRepositoryImpl()
    }
}
