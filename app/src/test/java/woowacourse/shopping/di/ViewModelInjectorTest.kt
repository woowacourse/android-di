package woowacourse.shopping.di

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

interface FakeRepository
class FakeRepositoryImpl : FakeRepository
class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel by viewModels<FakeViewModel> { getViewModelFactory<FakeViewModel>() }
}

@RunWith(RobolectricTestRunner::class)
class ViewModelInjectorTest {

    @Before
    fun setup() {
        Container.clear()
    }

    @Test
    fun `Container에서 타입에 맞는 instance를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val repository = FakeRepositoryImpl()
        Container.addInstance(FakeRepository::class, repository)
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        assertNotNull(viewModel)
        assertEquals(viewModel.fakeRepository, repository)
    }

    @Test(expected = NoSuchElementException::class)
    fun `Container에 타입에 맞는 instance가 없으면 ViewModel 의존성 주입에 실패하여 에러가 발생한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        activity.viewModel
    }
}
