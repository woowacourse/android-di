package woowacourse.shopping

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.bandal.fullmoon.AppContainer
import com.bandal.fullmoon.FullMoonInjector
import com.bandal.fullmoon.Qualifier

interface FakeRepository

class DefaultFakeRepository : FakeRepository

class FakeViewModelWithConstructor(
    @Qualifier("fakeConstructor")
    val repository: FakeRepository,
) : ViewModel()

class FakeViewModelWithField : ViewModel() {
    @Qualifier("fakeField")
    lateinit var repository: FakeRepository
}

class FakeActivityWithConstructor : AppCompatActivity() {
    val viewModel: FakeViewModelWithConstructor by lazy {
        ViewModelProvider(this, FakeViewModelFactory)[FakeViewModelWithConstructor::class.java]
    }
}

class FakeActivityWithField : AppCompatActivity() {
    val viewModel: FakeViewModelWithField by lazy {
        ViewModelProvider(this, FakeViewModelFactory)[FakeViewModelWithField::class.java]
    }
}

class FakeAppContainer : AppContainer() {
    @Qualifier("fakeConstructor")
    fun createFakeRepoHaveConstructor(): DefaultFakeRepository {
        return DefaultFakeRepository()
    }

    @Qualifier("fakeField")
    fun createFakeRepoHaveField(): DefaultFakeRepository {
        return DefaultFakeRepository()
    }
}

object FakeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FullMoonInjector(FakeAppContainer()).inject(modelClass.kotlin)
    }
}
