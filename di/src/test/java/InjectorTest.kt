import android.content.Context
import annotations.DatabaseFakeRepository
import annotations.InMemoryFakeRepository
import com.hyegyeong.di.DiContainer
import com.hyegyeong.di.DiModule
import com.hyegyeong.di.Injector
import com.hyegyeong.di.annotations.Inject
import com.hyegyeong.di.annotations.Singleton
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test

class InjectorTest {

    interface MultiImplFakeRepositoryInterface
    interface SingleImplFakeRepositoryInterface
    class FakeAppModule : DiModule {
        fun provideCartProductDao(): FakeDaoInterface = FakeDaoImpl()

        @Singleton
        @InMemoryFakeRepository
        fun provideInMemoryCartRepository(): MultiImplFakeRepositoryInterface =
            FakeInMemoryRepository()

        @Singleton
        @DatabaseFakeRepository
        fun provideDataBaseCartRepository(@Inject dao: FakeDaoInterface): MultiImplFakeRepositoryInterface =
            FakeDatabaseRepository(dao)
    }

    class FakeActivityModule : DiModule {
        fun provideCartProductDao(): FakeDaoInterface = FakeDaoImpl()
        fun provideFakeRepository() = FakeRepository()
        fun provideSingleImplFakeRepositoryImpl(@Inject dao: FakeDaoInterface): SingleImplFakeRepositoryInterface =
            SingleImplFakeRepositoryImpl(dao)
    }

    class FakeRepository
    class FakeDaoImpl : FakeDaoInterface
    class FakeInMemoryRepository : MultiImplFakeRepositoryInterface
    class FakeDatabaseRepository(dao: FakeDaoInterface) : MultiImplFakeRepositoryInterface
    class SingleImplFakeRepositoryImpl(dao: FakeDaoInterface) : SingleImplFakeRepositoryInterface

    @Before
    fun setUp() {
        DiContainer.appModule = FakeAppModule()
        DiContainer.dependencyModule = FakeActivityModule()
    }

    @Test
    fun `인터페이스가 아닌 클래스를 생성자로 주입받는 경우 생성자에 @Inject 키워드만 붙이면 된다`() {
        // given
        class FakeViewModel @Inject constructor(val fakeRepository: FakeRepository)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스를 생성자로 주입받는 경우, 구현체가 2개 이상인 경우, 생성자에 @Inject 어노테이션을 붙이고, 파라미터 앞에 원하는 어노테이션을 붙이면 된다`() {
        // given
        class FakeViewModel @Inject constructor(@Singleton @InMemoryFakeRepository val fakeRepository: MultiImplFakeRepositoryInterface)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스를 생성자로 주입받는 경우, 구현체가 하나인 경우, @Inject 어노테이션만 붙이면 된다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            lateinit var fakeRepository: SingleImplFakeRepositoryInterface
        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }

    @Test
    fun `인터페이스를 생성자로 주입받는 경우, 원하는 구현체가 또 다른 생성자를 주입받을 수 있다`() {
        // given
        class FakeViewModel @Inject constructor(@Singleton @DatabaseFakeRepository val fakeRepository: MultiImplFakeRepositoryInterface)

        // when
        Injector.inject<FakeViewModel>()
    }

    @Test
    fun `인터페이스가 아닌 클래스를 필드로 주입받는 경우 필드에 @Inject 키워드만 붙이면 된다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            lateinit var fakeRepository: FakeRepository
        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }

    @Test
    fun `인터페이스를 필드로 주입받는 경우, 필드에 @Inject 어노테이션을 붙이고, 필드 앞에 원하는 어노테이션을 붙이면 된다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            @InMemoryFakeRepository
            @Singleton
            lateinit var fakeRepository: MultiImplFakeRepositoryInterface
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
            @Singleton
            lateinit var fakeRepository: MultiImplFakeRepositoryInterface
        }

        // when
        Injector.inject<FakeFieldInjectViewModel>()
    }

    @Test
    fun `@Singleton 어노테이션이 붙은 의존성은, 그 의존성을 주입받는 여러 객체가 만들어져도 인스턴스를 공유한다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            @DatabaseFakeRepository
            @Singleton
            lateinit var fakeRepository: MultiImplFakeRepositoryInterface
        }

        class FakeFieldInjectViewModel2 {
            @Inject
            @DatabaseFakeRepository
            @Singleton
            lateinit var fakeRepository: MultiImplFakeRepositoryInterface
        }

        // when
        val viewmodel1 = Injector.inject<FakeFieldInjectViewModel>()
        val viewmodel2 = Injector.inject<FakeFieldInjectViewModel2>()

        // then
        assertEquals(viewmodel1.fakeRepository, viewmodel2.fakeRepository)
    }

    @Test
    fun `@Singleton 어노테이션이 붙지 않은 의존성은, 그 의존성을 주입받는 여러 객체가 만들어지면 다른 인스턴스가 생성된다`() {
        // given
        class FakeFieldInjectViewModel {
            @Inject
            lateinit var fakeRepository: FakeRepository
        }

        class FakeFieldInjectViewModel2 {
            @Inject
            lateinit var fakeRepository: FakeRepository
        }

        // when
        val viewmodel1 = Injector.inject<FakeFieldInjectViewModel>()
        val viewmodel2 = Injector.inject<FakeFieldInjectViewModel2>()

        // then
        assertNotEquals(viewmodel1.fakeRepository, viewmodel2.fakeRepository)
    }
}
