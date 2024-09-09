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
import woowacourse.shopping.util.injectedViewModels

interface FakeRepository

class FakeRepositoryImpl : FakeRepository

class FakeRepositoryImplNotInjected : FakeRepository

class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by injectedViewModels {
        ViewModelFactory(
            RepositoryModule(FakeRepository::class to FakeRepositoryImpl::class),
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
        val expected = FakeRepositoryImpl::class.java
        val incorrect = FakeRepositoryImplNotInjected::class.java

        assertThat(actual).isInstanceOf(expected)
        assertThat(actual).isNotInstanceOf(incorrect)
    }
}
