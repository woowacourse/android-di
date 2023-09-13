package woowacourse.shopping.data.di

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.jvm.jvmErasure

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class InjectorTest {
    object FakeContainer : Container {

        override val instances: MutableMap<AnnotationType, Any> = mutableMapOf()
        override fun addInstance(instance: Any) {
            val kclass = instance::class
            val annotations: List<Annotation> = kclass.annotations
            val annotationType =
                AnnotationType(annotations.getOrNull(0), kclass.supertypes[0].jvmErasure)
            instances[annotationType] = instance
        }

        override fun getInstance(annotationType: AnnotationType): Any? {
            return instances[annotationType]
        }

    }

    class DefaultFakeRepository : FakeRepository
    class RecursiveFakeRepository(@Inject fakeDao: FakeDao) : FakeRepository
    class DefaultFakeDao : FakeDao

    @Before
    fun setUp() {
        Injector.container = FakeContainer
    }

    @After
    fun tearDown() {
        FakeContainer.instances.clear()
    }

    @Test
    fun `의존성 모듈이 모두 제공될 때 자동 DI 가 성공하는지 테스트`() {
        // given
        val fakeRepository: FakeRepository = DefaultFakeRepository()
        FakeContainer.addInstance(fakeRepository)
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        val actual = activity.viewModel
        assertEquals(actual.fakeRepository, fakeRepository)
    }

    @Test(expected = NoSuchElementException::class)
    fun `의존성 모듈이 제공되지 않을 때 자동 DI 가 실패하는지 테스트`() {
        // given
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        activity.viewModel
    }

    @Test
    fun `재귀 DI 가 잘 작동하는지 테스트`() {
        //given
        FakeContainer.addInstance(DefaultFakeDao())
        FakeContainer.addInstance(Injector.inject<RecursiveFakeRepository>())
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        val actual = activity.viewModel
        assertEquals(
            FakeContainer.getInstance(AnnotationType(null, FakeRepository::class)),
            actual.fakeRepository
        )
    }

    @Test
    fun `의존성 주입이 필요한 필드에만 의존성을 주입한다`() {
        //given
        FakeContainer.addInstance(DefaultFakeRepository())
        val activity = Robolectric.buildActivity(FakeFieldInjectActivity::class.java).create().get()

        // then
        val actual = activity.viewModel
        assertEquals(
            FakeContainer.getInstance(AnnotationType(null, FakeRepository::class)),
            actual.productRepository
        )
    }
}

