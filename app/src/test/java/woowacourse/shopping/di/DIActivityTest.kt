package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

interface FakeRepository

class DefaultFakeRepository : FakeRepository

class FakeViewModel(val fakeRepository: FakeRepository) : ViewModel()

class FakeActivity : DIActivity() {
    lateinit var viewModel: FakeViewModel
}

class FakeApplication : DIApplication() {
    override fun inject() {
        repositoryContainer =
            RepositoryContainer.of(FakeRepository::class to DefaultFakeRepository())
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class DIActivityTest {

    @Test
    fun `맞는 타입의 객체 인스턴스가 존재하면 ViewModel 의존성 주입이 성공한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        val viewModel = activity?.viewModel

        assertThat(viewModel).isNotNull
    }
}
