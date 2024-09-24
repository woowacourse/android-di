import com.example.di.DIModule
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import com.example.fake.FakeCartDao
import com.example.fake.FakeCartRepository
import com.example.fake.FakeInMemoryCartRepository

class FakeApplicationModule : DIModule {
    @LifeCycle(LifeCycleScope.APPLICATION)
    fun provideFakeCartDao(): FakeCartDao {
        return FakeCartDao()
    }

    @LifeCycle(LifeCycleScope.APPLICATION)
    @Qualifier(QualifierType.Database)
    fun provideFakeDatabaseCartRepository(fakeCartDao: FakeCartDao): FakeCartRepository {
        return FakeDatabaseCartRepository(fakeCartDao)
    }

    @LifeCycle(LifeCycleScope.APPLICATION)
    @Qualifier(QualifierType.InMemory)
    fun provideFakeInMemoryCartRepository(): FakeCartRepository {
        return FakeInMemoryCartRepository()
    }
}
