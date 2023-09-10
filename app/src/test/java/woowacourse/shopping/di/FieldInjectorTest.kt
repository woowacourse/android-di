package woowacourse.shopping.di

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

interface FakeRepository

class DefaultFakeRepository : FakeRepository

object TestRepositoryContainer1 : Container {
    val fakeRepository: FakeRepository = DefaultFakeRepository()
}

class TestApplication1 : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = Injector(TestRepositoryContainer1)
    }

    companion object {
        lateinit var injector: Injector
    }
}

class FakeViewModel(val repository: FakeRepository) : ViewModel() {

    @InjectField
    lateinit var pingu: FakeRepository

    lateinit var otter: FakeRepository
}

class FakeActivity : AppCompatActivity() {
    val viewModel by getViewModel<FakeViewModel>(TestApplication1.injector)
}

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication1::class)
class FieldInjectorTest {

    @Test
    fun `InjectField 어노테이션이 붙은 필드에 필드 주입한다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        Assert.assertNotNull(viewModel.pingu)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `InjectField 어노테이션이 붙지 않은 필드에는 필드 주입하지 않는다`() {
        // given
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // when
        val viewModel = activity.viewModel

        // then
        Assert.assertNotNull(viewModel.otter)
    }
}
