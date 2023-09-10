package woowacourse.shopping.data.di

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.reflect.KClass

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class InjectorTest {
    object FakeContainer : Container {
        override val instances: MutableMap<KClass<*>, Any> = mutableMapOf()
        override fun addInstance(clazz: KClass<*>, instance: Any) {
            instances[clazz] = instance
        }

        override fun getInstance(clazz: KClass<*>): Any? {
            return instances[clazz]
        }

    }

    @Before
    fun setUp() {
        Injector.container = FakeContainer
    }

    @Test
    fun `의존성 모듈이 모두 제공될 때 자동 DI 가 성공하는지 테스트`() {
        // given
        val fakeRepository: FakeRepository = DefaultFakeRepository()
        FakeContainer.addInstance(FakeRepository::class, fakeRepository)
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
}

