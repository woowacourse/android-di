package woowacourse.shopping.di

import org.junit.Assert.assertEquals
import org.junit.Test

class InjectorTest {

    interface FakeRepository

    class DefaultFakeRepository : FakeRepository

    class FakeRepositoryContainer : Container {
        val fakeRepository: FakeRepository = DefaultFakeRepository()
    }

    class FakeRepositoryContainer2 : Container

    class FakeViewModel(val fakeRepository: FakeRepository)

    interface FakeViewModel2

    private val injector = Injector(FakeRepositoryContainer())

    @Test
    fun `주어진 클래스 타입에 맞게 인스턴스를 생성해서 반환한다`() {
        // when
        val actual = injector.getInstance<FakeViewModel>()

        // then
        assertEquals(FakeViewModel::class, actual::class)
    }

    @Test(expected = IllegalStateException::class)
    fun `주어진 클래스 타입의 주생성자를 가져올 수 없다면 오류가 발생한다`() {
        injector.getInstance<FakeViewModel2>()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `container에 없는 타입을 요구할 경우 오류가 발생한다`() {
        // given
        val injector = Injector(FakeRepositoryContainer2())

        // when
        injector.getInstance<FakeViewModel>()
    }
}
