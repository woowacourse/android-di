import com.example.di.annotation.Inject
import com.example.fake.FakeCartDao
import com.example.fake.FakeCartRepository
import com.example.fake.FakeProductRepository

class FakeDatabaseCartRepository(
    @Inject val fakeCartDao: FakeCartDao,
    val fakeProductRepository: FakeProductRepository? = null,
) : FakeCartRepository
