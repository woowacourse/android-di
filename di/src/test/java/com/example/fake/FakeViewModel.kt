import androidx.lifecycle.ViewModel
import com.example.di.annotation.Inject
import com.example.di.annotation.Qualifier
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository

class FakeViewModel(
    @Qualifier(FakeDatabaseCartRepository::class) @Inject val fakeDatabaseCartRepository: FakeCartRepository,
) : ViewModel() {
    @Inject
    lateinit var fakeFieldRepository: FakeFieldRepository
}
