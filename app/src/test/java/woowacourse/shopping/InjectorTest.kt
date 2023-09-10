package woowacourse.shopping

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.annotation.Injected
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.ui.util.createViewModel
import kotlin.reflect.KClass

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class InjectorTest {

    @After
    fun tearDown() {
        TestContainer.clearInstance()
    }

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeRepository: FakeRepository = DefaultFakeRepository()
        TestContainer.createInstance(FakeRepository::class, fakeRepository)

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertEquals(viewModel.repository, fakeRepository)
    }

    @Test
    fun `적절한 객체 인스턴스를 재귀적으로 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeDao: FakeDao = DefaultFakeDao()
        TestContainer.createInstance(FakeDao::class, fakeDao)

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val fakeRepository: FakeRepository =
            Injector(TestContainer).create(FakeDaoRepository::class)
        TestContainer.createInstance(FakeRepository::class, fakeRepository)
        assertNotNull(fakeRepository)

        val viewModel = activity.viewModel
        assertNotNull(viewModel)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `적절한 객체 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        activity.viewModel
    }

    @Test
    fun `적절한 객체 인스턴스를 찾아 ViewModel 프로퍼티와 필드에 의존성을 주입한다`() {
        // given
        val fakeDao: FakeDao = DefaultFakeDao()
        TestContainer.createInstance(FakeDao::class, fakeDao)

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val fakeRepository: FakeRepository =
            Injector(TestContainer).create(FakeFieldRepository::class)
        TestContainer.createInstance(FakeRepository::class, fakeRepository)
        assertNotNull(fakeRepository)
        assertNotNull(fakeRepository::class.java.getDeclaredField("fieldDao"))

        val viewModel = activity.viewModel
        assertNotNull(viewModel)
    }
}

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val container = TestContainer

        injector = Injector(container)
    }

    companion object {
        lateinit var injector: Injector
    }
}

object TestContainer : ShoppingContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    override fun <T : Any> createInstance(clazz: KClass<*>, instance: T) {
        instances[clazz] = instance
    }

    override fun <T : Any> getInstance(clazz: KClass<T>): T? {
        return instances[clazz] as? T
    }

    override fun clearInstance() {
        instances.clear()
    }
}

interface FakeDao
class DefaultFakeDao : FakeDao

interface FakeRepository
class DefaultFakeRepository : FakeRepository
class FakeDaoRepository(
    @Injected val dao: FakeDao,
) : FakeRepository

class FakeFieldRepository(
    @Injected val propertyDao: FakeDao,
) : FakeRepository {
    @Injected
    private lateinit var fieldDao: FakeDao
    private lateinit var fakeName: String
}

class FakeViewModel(
    @Injected val repository: FakeRepository,
) : ViewModel() {
    companion object {
        val factory = viewModelFactory {
            initializer {
                TestApplication.injector.create(FakeViewModel::class)
            }
        }
    }
}

class FakeActivity : AppCompatActivity() {
    val viewModel: FakeViewModel by createViewModel(FakeViewModel.factory)
}
