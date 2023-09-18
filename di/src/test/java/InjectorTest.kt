import annotations.DatabaseFakeRepository
import annotations.InMemoryFakeRepository
import com.hyegyeong.di.DependencyContainer
import com.hyegyeong.di.Injector
import com.hyegyeong.di.annotations.Inject
import org.junit.Before
import org.junit.Test

class InjectorTest {

    interface FakeRepositoryInterface
    class FakeRepository
    class FakeDaoImpl : FakeDaoInterface
    class FakeInMemoryRepository : FakeRepositoryInterface
    class FakeDatabaseRepository(dao: FakeDaoInterface) : FakeRepositoryInterface
    class FakeContainer : DependencyContainer {

        fun provideCartProductDao(): FakeDaoInterface = FakeDaoImpl()

        @InMemoryFakeRepository
        fun provideInMemoryCartRepository(): FakeRepositoryInterface = FakeInMemoryRepository()

        @DatabaseFakeRepository
        fun provideDataBaseCartRepository(dao: FakeDaoInterface): FakeRepositoryInterface =
            FakeDatabaseRepository(dao)

        fun provideFakeRepository() = FakeRepository()

    }

    @Before
    fun setUp() {
        Injector.container = FakeContainer()
    }

    @Test
    fun `인터페이스가 아닌 클래스를 생성자로 주입받는 경우 생성자에 @Inject 키워드만 붙이면 된다`() {
        // given
        class FakeViewModel @Inject constructor(val fakeRepository: FakeRepository)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스를 생성자로 주입받는 경우, 생성자에 @Inject 어노테이션을 붙이고, 파라미터 앞에 원하는 어노테이션을 붙이면 된다`() {
        // given
        class FakeViewModel @Inject constructor(@InMemoryFakeRepository val fakeRepository: FakeRepositoryInterface)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스를 생성자로 주입받는 경우, 원하는 구현체가 또 다른 생성자를 주입받을 수 있다`() {
        // given
        class FakeViewModel @Inject constructor(@DatabaseFakeRepository val fakeRepository: FakeRepositoryInterface)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스가 아닌 클래스를 필드로 주입받는 경우 필드에 @Inject 키워드만 붙이면 된다`() {
        //given
        class FakeFieldInjectViewModel {
            @Inject
            lateinit var fakeRepository: FakeRepository

        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }

    @Test
    fun `인터페이스를 필드로 주입받는 경우, 필드에 @Inject 어노테이션을 붙이고, 필드 앞에 원하는 어노테이션을 붙이면 된다`() {
        //given
        class FakeFieldInjectViewModel {
            @Inject
            @InMemoryFakeRepository
            lateinit var fakeRepository: FakeRepositoryInterface
        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }

    @Test
    fun `인터페이스를 필드로 주입받는 경우, 원하는 구현체가 또 다른 생성자를 주입받을 수 있다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            @DatabaseFakeRepository
            lateinit var fakeRepository: FakeRepositoryInterface
        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }
}