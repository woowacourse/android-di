package woowacourse.shopping.di

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.reflect.jvm.jvmErasure

class ClassA(
    @WoogiProperty
    val arg1: ClassB
)

class ClassB(val arg1: ClassC)
class ClassC

class ClassD(val arg1: ClassE)
class ClassE(val arg1: ClassF)
class ClassF

class DependencyInjectorTest {

    @Test
    fun `생성자에 인자가 있는 경우 자동으로 의존성 주입을 수행한다`() {
        // given
        val injector = DependencyInjector()

        // when
        val instanceB = injector.inject<ClassB>()
        val actual = instanceB::arg1.returnType.jvmErasure

        // then
        val expected = ClassC::class

        assertEquals(expected, actual)
    }

    @Test
    fun `생성자의 인자의 생성자에 인자가 있는 경우 자동으로 의존성 주입을 수행한다`() {
        // given
        val injector = DependencyInjector()

        // when
        val instanceD = injector.inject<ClassD>()
        val actual1 = instanceD::arg1.returnType.jvmErasure
        val actual2 = instanceD.arg1::arg1.returnType.jvmErasure

        // then
        val expected1 = ClassE::class
        val expected2 = ClassF::class

        assertEquals(expected1, actual1)
        assertEquals(expected2, actual2)
    }

    @Test
    fun `생성자의 인자에 WoogiAnnotation이 붙어있는 프로퍼티의 경우 Container로부터 해당 프로퍼티 타입의 인스턴스를 가져온다`() {
        // given
        val container: Container = mockk()
        val injector = DependencyInjector(container)

        every {
            container.find(ClassB::class)
        } returns ClassB(ClassC())

        // when
        val instanceA = injector.inject<ClassA>()
        val actual = instanceA::arg1.returnType.jvmErasure

        // then
        val expected = ClassB::class
        assertEquals(expected, actual)
        verify { container.find(ClassB::class) }
    }

    @Test(expected = NoSuchElementException::class)
    fun `생성자의 인자에 WoogiAnnotation이 붙어있는 프로퍼티의 경우 Container에 존재하지 않으면 예외가 발생한다`() {
        // given
        val container: Container = mockk()
        val injector = DependencyInjector(container)

        every {
            container.find(ClassB::class)
        } returns null

        // when
        injector.inject<ClassA>()

        // then
    }
}
