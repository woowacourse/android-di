import com.example.di.annotation.ChadInject
import com.example.di.annotation.ChadQualifier
import com.example.di.annotation.QualifierType
import com.example.fake.FakeActivityModule
import com.example.fake.FakeCartRepository
import com.example.fake.FakeDIViewModel
import com.example.fake.FakeFieldRepository
import com.example.fake.FakeProductRepository

class FakeViewModel(
    @ChadQualifier(QualifierType.Database) @ChadInject val fakeCartRepository: FakeCartRepository,
    @ChadInject val fakeProductRepository: FakeProductRepository,
) : FakeDIViewModel(FakeActivityModule::class) {
    @ChadInject
    lateinit var fakeFieldRepository: FakeFieldRepository
}
