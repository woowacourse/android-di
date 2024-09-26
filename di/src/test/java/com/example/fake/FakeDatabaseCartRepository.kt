import com.example.di.annotation.ChadInject
import com.example.fake.FakeCartDao
import com.example.fake.FakeCartRepository
import com.example.fake.FakeProductRepository

class FakeDatabaseCartRepository(
    @ChadInject val fakeCartDao: FakeCartDao,
    val fakeProductRepository: FakeProductRepository? = null,
) : FakeCartRepository
