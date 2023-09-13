package woowacourse.shopping

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.di.annotation.Injected
import woowacourse.shopping.di.annotation.Qualifier
import woowacourse.shopping.di.annotation.Qualifier.Companion.DATABASE
import woowacourse.shopping.di.annotation.Qualifier.Companion.IN_MEMORY
import woowacourse.shopping.di.container.ShoppingContainer
import woowacourse.shopping.di.injector.Injector
import woowacourse.shopping.ui.util.createViewModel
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class InjectorTest {
    @Test
    fun `repository를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeRepository: FakeRepository = InMemoryFakeRepository()
        TestApplication.container.createInstance(FakeRepository::class, fakeRepository)

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
    fun `dao, repository 인스턴스를 재귀적으로 찾아 ViewModel 의존성을 주입한다`() {
        // given
        TestApplication.container.createInstance(FakeDao::class, DefaultFakeDao())
        TestApplication.container.createInstance(
            FakeRepository::class,
            TestApplication.injector.create(DefaultFakeRepository::class)
        )

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertNotNull(viewModel.repository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `repository 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()
        activity.viewModel
    }

    @Test
    fun `@Inject가 붙은 파라미터와 private 필드의 인스턴스만 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeDao: FakeDao = DefaultFakeDao()
        TestApplication.container.createInstance(FakeDao::class, fakeDao)
        TestApplication.container.createInstance(
            FakeRepository::class,
            TestApplication.injector.create(DatabaseFakeRepository::class)
        )

        val activity = Robolectric
            .buildActivity(FakeActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel)
        assertNotNull(viewModel.repository)
        viewModel.repository::class.java.getDeclaredField("fieldDao").run {
            isAccessible = true
            assertNotNull(this.get(viewModel.repository))
        }
        viewModel.repository::class.java.getDeclaredField("fakeName").run {
            isAccessible = true
            assertNull(this.get(viewModel.repository))
        }
    }

    @Test
    fun `파라미터와 private 필드에 @Qualifier를 구분하여 적절한 인스턴스를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeDao: FakeDao = DefaultFakeDao()
        TestApplication.container.createInstance(FakeDao::class, fakeDao)
        TestApplication.container.createInstance(
            IN_MEMORY,
            TestApplication.injector.create(InMemoryFakeRepository::class)
        )
        TestApplication.container.createInstance(
            DATABASE,
            TestApplication.injector.create(DatabaseFakeRepository::class)
        )

        val activity = Robolectric
            .buildActivity(FakeQualifierActivity::class.java)
            .create()
            .get()

        // then
        val viewModel = activity.viewModel
        assertNotNull(viewModel.inMemoryRepository)
        assert(viewModel.inMemoryRepository is InMemoryFakeRepository)
        viewModel::class.java.getDeclaredField("databaseRepository").run {
            isAccessible = true
            assertNotNull(this.get(viewModel))
            assert(this.get(viewModel) is DatabaseFakeRepository)
        }
    }
}

class TestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        container = TestContainer()
        injector = Injector(container)
    }

    companion object {
        lateinit var container: ShoppingContainer
        lateinit var injector: Injector
    }
}

class TestContainer : ShoppingContainer {
    private val instances = mutableMapOf<String, Any>()

    override fun <T : Any> createInstance(clazz: KClass<*>, instance: T) {
        instances[clazz.jvmName] = instance
    }

    override fun <T : Any> createInstance(qualifier: String, instance: T) {
        instances[qualifier] = instance
    }

    override fun <T : Any> getInstance(clazz: KClass<T>): T? {
        return instances[clazz.jvmName] as? T
    }

    override fun <T : Any> getInstance(qualifier: String): T? {
        return instances[qualifier] as? T
    }
}

interface FakeDao
class DefaultFakeDao : FakeDao

interface FakeRepository

class DefaultFakeRepository(
    @Injected val dao: FakeDao,
) : FakeRepository

class InMemoryFakeRepository : FakeRepository

class DatabaseFakeRepository(
    @Injected val propertyDao: FakeDao,
) : FakeRepository {
    @Injected
    private var fieldDao: FakeDao? = null
    private var fakeName: String? = null
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

class FakeQualifierViewModel(
    @Injected @Qualifier(IN_MEMORY) val inMemoryRepository: FakeRepository,
) : ViewModel() {
    @Injected
    @Qualifier(DATABASE)
    private var databaseRepository: FakeRepository? = null // 테스트를 용이하게 하기 위해 null

    companion object {
        val factory = viewModelFactory {
            initializer {
                TestApplication.injector.create(FakeQualifierViewModel::class)
            }
        }
    }
}

class FakeQualifierActivity : AppCompatActivity() {
    val viewModel: FakeQualifierViewModel by createViewModel(FakeQualifierViewModel.factory)
}
