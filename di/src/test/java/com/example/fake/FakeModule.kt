import com.example.di.Module
import com.example.fake.FakeCartDao
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository
import com.example.fake.FakeInMemoryCartRepository
import com.example.fake.FakeProductRepository

class FakeModule : Module {
    fun provideFakeCartDao(): FakeCartDao {
        return FakeCartDao()
    }

    fun provideFakeDatabaseCartRepository(
        fakeCartDao: FakeCartDao,
        fakeProductRepository: FakeProductRepository,
    ): FakeCartRepository {
        return FakeDatabaseCartRepository(fakeCartDao, fakeProductRepository)
    }

    fun provideFakeInMemoryCartRepository(): FakeCartRepository {
        return FakeInMemoryCartRepository()
    }

    fun provideFakeProductRepository(): FakeProductRepository {
        return FakeProductRepository()
    }

    fun provideFakeFieldRepository(): FakeFieldRepository {
        return FakeFieldRepository()
    }
}
