import com.example.di.DIModule
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import com.example.fake.FakeCartDao
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository
import com.example.fake.FakeInMemoryCartRepository
import com.example.fake.FakeProductRepository

class FakeModule : DIModule {
    fun provideFakeCartDao(): FakeCartDao {
        return FakeCartDao()
    }

    @Qualifier(QualifierType.Database)
    fun provideFakeDatabaseCartRepository(
        fakeCartDao: FakeCartDao,
        fakeProductRepository: FakeProductRepository,
    ): FakeCartRepository {
        return FakeDatabaseCartRepository(fakeCartDao, fakeProductRepository)
    }

    @Qualifier(QualifierType.InMemory)
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
