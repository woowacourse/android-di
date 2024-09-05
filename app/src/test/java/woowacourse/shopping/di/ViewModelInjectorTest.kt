package woowacourse.shopping.di

import androidx.appcompat.app.AppCompatActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.RepositoryModule
import woowacourse.shopping.util.injectedViewModels

interface FakeRepository

class FakeRepositoryImpl private constructor() : FakeRepository {
    companion object {
        val instance: FakeRepositoryImpl by lazy { FakeRepositoryImpl() }
    }
}

class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by injectedViewModels {
        ViewModelFactory(
            ViewModelInjector(
                RepositoryModule(FakeRepository::class to FakeRepositoryImpl.instance),
            ),
        )
    }
}

@RunWith(RobolectricTestRunner::class)
class ViewModelInjectorTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activity: FakeActivity

    @Before
    fun setup() {
        activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()
    }

    @Test
    fun `알맞은 instance를 주입 한다`() {
        val actual = activity.viewModel.fakeRepository
        val expected = FakeRepositoryImpl.instance

        assertThat(actual).isEqualTo(expected)
    }
}
