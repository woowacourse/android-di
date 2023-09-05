package woowacourse.shopping.data.di

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InjectorTest {
    lateinit var injector: Injector

    @Test
    fun `의존성 모듈이 모두 제공될 때 자동 DI 가 성공하는지 테스트`() {
        // given
        injector = Injector(listOf(object : Module {
            fun getString(): String = ""
        }))

        // when
        val actual: FakeViewModel = injector.inject()

        // then
        assertEquals(actual::class, FakeViewModel::class)
    }

    @Test(expected = NoSuchElementException::class)
    fun `의존성 모듈이 제공되지 않을 때 자동 DI 가 실패하는지 테스트`() {
        // given
        injector = Injector(listOf(object : Module {
        }))

        // when
        injector.inject<FakeViewModel>()
    }
}

