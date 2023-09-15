package woowacourse.shopping.di

import org.junit.Assert.assertEquals
import org.junit.Test

class ModuleTest {
    interface FakeInterface

    class FakeClassA : FakeInterface

    class FakeClassB : FakeInterface

    class FakeClassC(fakeInterface: FakeInterface)

    class FakeModule : Module {
        fun provideClassA(): FakeInterface = FakeClassA()

        fun provideClassB(): FakeInterface = FakeClassB()

        fun provideClassC(fakeInterface: FakeInterface): FakeClassC = FakeClassC(fakeInterface)
    }

    @Test
    fun `모듈에 전체 함수 3개 중 추상화한 객체를 제공하는 함수가 두 개일 때 두 함수를 가져올 수 있다`() {
        // given
        val module = FakeModule()

        // when
        val actual = module.searchFunctions(FakeInterface::class).map { it.returnType.classifier }

        // then
        val expected = listOf(FakeInterface::class, FakeInterface::class)
        assertEquals(expected, actual)
    }

    @Test
    fun `모듈에 전체 함수 3개 중 FakeClassC를 반환하는 함수를 가져올 수 있다`() {
        // given
        val module = FakeModule()

        // when
        val actual = module.searchFunction(FakeClassC::class).returnType.classifier

        // then
        val expected = FakeClassC::class
        assertEquals(expected, actual)
    }
}
