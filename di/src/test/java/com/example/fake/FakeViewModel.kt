import androidx.lifecycle.ViewModel
import com.example.di.annotation.Database
import com.example.di.annotation.Inject
import com.example.fake.FakeCartRepository
import com.example.fake.FakeFieldRepository

class FakeViewModel(
    @Database val fakeDatabaseCartRepository: FakeCartRepository,
) : ViewModel() {
    @Inject
    lateinit var fakeFieldRepository: FakeFieldRepository
}
