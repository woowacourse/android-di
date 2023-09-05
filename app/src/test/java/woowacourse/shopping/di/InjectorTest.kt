package woowacourse.shopping.di

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.KParameter
import kotlin.reflect.full.primaryConstructor

class InjectorTest {

    interface FakeRepository

    class DefaultFakeRepository : FakeRepository

    class FakeRepositoryContainer : Container {
        val fakeRepository: FakeRepository = DefaultFakeRepository()
    }

    class FakeViewModel(val fakeRepository: FakeRepository)

    private val injector = Injector(FakeRepositoryContainer())

    @Test
    fun `주어진 클래스 타입에 맞게 인스턴스를 생성해서 반환한다`() {
        // when
        val actual = injector.getInstance<FakeViewModel>()

        // then
        assertEquals(FakeViewModel::class, actual::class)
    }

    @Test
    fun `주어진 파라미터들에 따라 인자들을 만들어서 반환한다`() {
        // given
        val parameters: List<KParameter> = FakeViewModel::class.primaryConstructor?.parameters
            ?: throw java.lang.IllegalArgumentException()

        // when
        val actual = injector.getArguments(parameters)

        // then
        assertEquals(FakeRepositoryContainer().fakeRepository::class, actual.first()::class)
    }
}
