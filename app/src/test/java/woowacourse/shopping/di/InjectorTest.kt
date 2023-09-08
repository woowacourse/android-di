package woowacourse.shopping.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import java.lang.IllegalArgumentException

class FakeDatabase
class DefaultFakeRepository(private val database: FakeDatabase) {
    @Inject
    val items: List<String> = emptyList()
}

class FakeViewModel(
    val fakeRepository: DefaultFakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel> { viewModelFactory }
}

@RunWith(RobolectricTestRunner::class)
class InjectorTest {

    @Before
    fun setup() {
        Container.clear()
    }

    @Test
    fun `Container에서 타입에 맞는 instance를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val repository = DefaultFakeRepository(FakeDatabase())
        Container.addInstance(DefaultFakeRepository::class, repository)
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.fakeRepository)
        assertNotNull(viewModel.fakeRepository.items)
    }

    @Test
    fun `주입해야하는 인스턴스를 Container에서 찾을 수 없다면 Injector로 생성해서 주입한다`() {
        // given
        Container.addInstance(List::class, listOf("item1", "item2", "item3"))

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.fakeRepository)
        assertNotNull(viewModel.fakeRepository.items)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `주입해야하는 인스턴스를 Container에서 찾고 Injector로 생성할 수 없다면 에러를 띄운다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        activity.viewModel
    }
}
