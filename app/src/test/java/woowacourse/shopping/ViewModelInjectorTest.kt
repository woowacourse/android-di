package woowacourse.shopping

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.Container
import woowacourse.shopping.di.Injector
import woowacourse.shopping.di.getViewModel

interface FakeRepository

class DefaultFakeRepository : FakeRepository

class FakeViewModel(
    val fakeRepository: FakeRepository,
) : ViewModel()

class FakeActivity : AppCompatActivity() {
    val viewModel by getViewModel<FakeViewModel>(TestApplication.injector)
}

interface FakeRepository2

class FakeViewModel1(
    val fakeRepository2: FakeRepository2,
) : ViewModel()

class FakeActivity2 : AppCompatActivity() {
    val viewModel by getViewModel<FakeViewModel1>(TestApplication.injector)
}

object TestRepositoryContainer : Container {
    val fakeRepository: FakeRepository = DefaultFakeRepository()
}

class TestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(TestRepositoryContainer)
    }

    companion object {
        lateinit var injector: Injector
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ViewModelInjectorTest {

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        assertNotNull(activity)

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertEquals(DefaultFakeRepository::class, viewModel.fakeRepository::class)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity2::class.java)
            .create()
            .get()
        activity.viewModel
    }
}
