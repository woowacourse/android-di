import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import com.example.di.viewmodel.DIViewModel
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository

class FakeViewModel(
    @Qualifier(QualifierType.Database) @Inject val fakeDatabaseCartRepository: FakeCartRepository,
) : DIViewModel(FakeModule::class) {
    @Inject
    lateinit var fakeFieldRepository: FakeFieldRepository
}
