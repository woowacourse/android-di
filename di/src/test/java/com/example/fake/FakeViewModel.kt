import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import com.example.fake.FakeActivityModule
import com.example.fake.FakeCartRepository
import com.example.fake.FakeDIViewModel
import com.example.fake.FakeFieldRepository
import com.example.fake.FakeProductRepository

class FakeViewModel(
    @Qualifier(QualifierType.Database) @Inject val fakeCartRepository: FakeCartRepository,
    @Inject val fakeProductRepository: FakeProductRepository,
) : FakeDIViewModel(FakeActivityModule::class) {
    @Inject
    lateinit var fakeFieldRepository: FakeFieldRepository
}
