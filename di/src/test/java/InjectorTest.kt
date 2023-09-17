//import com.hyegyeong.di.AnnotationType
//import com.hyegyeong.di.Container
//import com.hyegyeong.di.Injector
//import com.hyegyeong.di.annotations.Inject
//import com.hyegyeong.di.annotations.Qualifier
//import org.junit.After
//import org.junit.Assert
//import org.junit.Before
//import org.junit.Test
//import kotlin.reflect.KClass
//import kotlin.reflect.jvm.jvmErasure
//
//class InjectorTest {
//    object FakeContainer : Container {
//
//        override val instances: MutableMap<AnnotationType, Any> = mutableMapOf()
//        override fun addInstance(instance: Any) {
//            val kclass = instance::class
//            val annotations: List<Annotation> = kclass.annotations
//            val annotationType =
//                AnnotationType(annotations.getOrNull(0), kclass.supertypes[0].jvmErasure)
//            instances[annotationType] = instance
//        }
//
//        override fun getInstance(annotationType: AnnotationType): Any? {
//            return instances[annotationType]
//        }
//
//        override fun hasDuplicateObjectsOfType(kClass: KClass<*>): Boolean {
//            TODO("Not yet implemented")
//        }
//
//    }
//
//    @Qualifier("InMemory")
//    class DefaultFakeRepository : FakeRepository
//
//    @Qualifier("RoomDB")
//    class FakeRoomDBRepository(@Inject fakeDao: FakeDao) : FakeRepository
//    class DefaultFakeDao : FakeDao
//
//    @Before
//    fun setUp() {
//        Injector.container = FakeContainer
//    }
//
//    @After
//    fun tearDown() {
//        FakeContainer.instances.clear()
//    }
//
//    @Test
//    fun `의존성 모듈이 모두 제공될 때 자동 DI 가 성공하는지 테스트`() {
//        // given
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
//}