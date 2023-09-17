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

//    @Test(expected = NoSuchElementException::class)
//    fun `의존성 모듈이 제공되지 않을 때 자동 DI 가 실패하는지 테스트`() {
//        // given
//
//        // when
//        Injector.inject<FakeViewModel>()
//    }
//
//    @Test
//    fun `재귀 DI 가 잘 작동하는지 테스트`() {
//        //given
//        FakeContainer.addInstance(DefaultFakeDao())
//        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
//
//        // when
//        val actual = Injector.inject<FakeViewModel>()
//
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("RoomDB"), FakeRepository::class)),
//            actual.fakeRepository
//        )
//    }
//
//    @Test
//    fun `의존성 주입이 필요한 필드에만 의존성을 주입한다`() {
//        //given
//        FakeContainer.addInstance(DefaultFakeRepository())
//
//        // when
//        val actual = Injector.inject<FakeFieldInjectViewModel>()
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
//            actual.productRepository
//        )
//    }
//
////    @Test
////    fun `같은 타입의 의존성이 두 개 있을 때 어노테이션으로 주입할 의존성을 구분한다`() {
////        // given
////        FakeContainer.addInstance(DefaultFakeRepository())
////        FakeContainer.addInstance(DefaultFakeDao())
////        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
////
////        // when
////        val actual = Injector.inject<FakeFieldInjectViewModel>()
////
////        // then
////        Assert.assertEquals(
////            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
////            actual.productRepository
////        )
////    }
//
//    @Test
//    fun `인스턴스 1개면 @Inject 키워드로 주입`() {
//        // given
//        FakeContainer.addInstance(DefaultFakeRepository())
//        FakeContainer.addInstance(DefaultFakeDao())
//        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
//
//        // when
//        val actual = Injector.inject<FakeFieldInjectViewModel>()
//
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
//            actual.productRepository
//        )
//    }
//
//    @Test
//    fun `인스턴스 2개일때 @Inject 키워드 쓰면 예외`() {
//        // given
//        FakeContainer.addInstance(DefaultFakeRepository())
//        FakeContainer.addInstance(DefaultFakeDao())
//        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
//
//        // when
//        val actual = Injector.inject<FakeFieldInjectViewModel>()
//
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
//            actual.productRepository
//        )
//    }
//
//    @Test
//    fun `인스턴스 2개면 @Qulifier 키워드로 주입`() {
//        // given
//        FakeContainer.addInstance(DefaultFakeRepository())
//        FakeContainer.addInstance(DefaultFakeDao())
//        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
//
//        // when
//        val actual = Injector.inject<FakeFieldInjectViewModel>()
//
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
//            actual.productRepository
//        )
//    }
//
//    @Test
//    fun `인스턴스 1개일때 @Qulifier 키워드 쓰면 에러`() {
//        // given
//        FakeContainer.addInstance(DefaultFakeRepository())
//        FakeContainer.addInstance(DefaultFakeDao())
//        FakeContainer.addInstance(Injector.inject<FakeRoomDBRepository>())
//
//        // when
//        val actual = Injector.inject<FakeFieldInjectViewModel>()
//
//        // then
//        Assert.assertEquals(
//            FakeContainer.getInstance(AnnotationType(Qualifier("InMemory"), FakeRepository::class)),
//            actual.productRepository
//        )
//    }
}