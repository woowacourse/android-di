package woowacourse.shopping.di.injector

import com.ssu.di.annotation.Injected
import com.ssu.di.annotation.Qualifier
import com.ssu.di.container.DiContainer
import com.ssu.di.injector.Injector
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.KClass
import kotlin.reflect.jvm.jvmName

class InjectorTest {
    private lateinit var container: TestContainer
    private lateinit var parentContainer: TestContainer
    private lateinit var injector: Injector

    @BeforeEach
    fun setup() {
        parentContainer = TestContainer(null)
        container = TestContainer(parentContainer)

        injector = Injector(container)
    }

    @Test
    fun `repository를 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeRepository: FakeRepository = InMemoryFakeRepository()
        container.createInstance(FakeRepository::class, fakeRepository)

        // when
        val viewModel = injector.create(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertEquals(viewModel.repository, fakeRepository)
    }

    @Test
    fun `repository 인스턴스가 존재하지 않으면 ViewModel 의존성 주입에 실패한다`() {
        // when
        val exception = assertThrows<IllegalArgumentException> {
            injector.create(FakeViewModel::class)
        }

        // given
        assertEquals("FakeRepository 컨테이너에 해당 인스턴스가 존재하지 않습니다", exception.message)
    }

    @Test
    fun `dao, repository 인스턴스를 재귀적으로 찾아 ViewModel 의존성을 주입한다`() {
        // given
        container.createInstance(FakeDao::class, DefaultFakeDao())
        container.createInstance(
            FakeRepository::class,
            injector.create(DefaultFakeRepository::class)
        )

        // when
        val viewModel = injector.create(FakeViewModel::class)

        // then
        assertNotNull(viewModel)
        assertNotNull(viewModel.repository)
    }

    @Test
    fun `@Inject가 붙은 파라미터와 private 필드의 인스턴스만 찾아 ViewModel 의존성을 주입한다`() {
        // given
        val fakeDao: FakeDao = DefaultFakeDao()
        container.createInstance(FakeDao::class, fakeDao)
        container.createInstance(
            FakeRepository::class,
            injector.create(DatabaseFakeRepository::class)
        )

        // when
        val viewModel = injector.create(FakeViewModel::class)

        // then
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
        container.createInstance(FakeDao::class, fakeDao)
        container.createInstance(
            "A",
            injector.create(InMemoryFakeRepository::class)
        )
        container.createInstance(
            "B",
            injector.create(DatabaseFakeRepository::class)
        )

        // when
        val viewModel = injector.create(FakeQualifierViewModel::class)

        // then
        assertNotNull(viewModel.inMemoryRepository)
        assert(viewModel.inMemoryRepository is InMemoryFakeRepository)
        viewModel::class.java.getDeclaredField("databaseRepository").run {
            isAccessible = true
            assertNotNull(this.get(viewModel))
            assert(this.get(viewModel) is DatabaseFakeRepository)
        }
    }

    @Test
    fun `container에 존재하지 않는 인스턴스는 parentContainer에서 찾아 ViewModel 의존성을 주입한다`() {
        // given
        container.apply {
            createInstance(FakeDao::class, DefaultFakeDao())
            createInstance(
                "A",
                injector.create(InMemoryFakeRepository::class)
            )

        }
        parentContainer.apply {
            createInstance(
                "B",
                injector.create(DatabaseFakeRepository::class)
            )
        }

        // when
        val viewModel = injector.create(FakeQualifierViewModel::class)

        // then
        assertNotNull(viewModel.inMemoryRepository)
        assert(viewModel.inMemoryRepository is InMemoryFakeRepository)
        viewModel::class.java.getDeclaredField("databaseRepository").run {
            isAccessible = true
            assertNotNull(this.get(viewModel))
            assert(this.get(viewModel) is DatabaseFakeRepository)
        }
    }
}

class TestContainer(override val parentContainer: DiContainer?) : DiContainer {
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

class DefaultFakeRepository @Injected constructor(
    val dao: FakeDao,
) : FakeRepository

class InMemoryFakeRepository : FakeRepository

class DatabaseFakeRepository @Injected constructor(
    val propertyDao: FakeDao,
) : FakeRepository {
    @Injected
    private var fieldDao: FakeDao? = null
    private var fakeName: String? = null
}

class FakeViewModel @Injected constructor(
    val repository: FakeRepository,
)

class FakeQualifierViewModel @Injected constructor(
    @Qualifier("A") val inMemoryRepository: FakeRepository,
) {
    @Injected
    @Qualifier("B")
    private var databaseRepository: FakeRepository? = null // 테스트를 용이하게 하기 위해 null
}