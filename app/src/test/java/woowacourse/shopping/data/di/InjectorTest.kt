package woowacourse.shopping.data.di

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.IllegalArgumentException

@Config(application = FakeApplication::class)
@RunWith(RobolectricTestRunner::class)
class InjectorTest {

    @Before
    fun setUp() {
        Injector.modules = listOf()
    }

    @Test
    fun `의존성 모듈이 모두 제공될 때 자동 DI 가 성공하는지 테스트`() {
        // given
        val fakeRepository: FakeRepository = DefaultFakeRepository()
        Injector.modules = listOf(object : Module {
            val fakeRepository = fakeRepository
        })
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        val actual = activity.viewModel
        assertEquals(actual.fakeRepository, fakeRepository)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `의존성 모듈이 제공되지 않을 때 자동 DI 가 실패하는지 테스트`() {
        // given
        Injector.modules = listOf()
        val activity = Robolectric.buildActivity(FakeActivity::class.java).create().get()

        // then
        activity.viewModel
    }
}

