import com.example.woogi_di.WoogiContainer
import com.example.woogi_di.WoogiInjector
import com.example.woogi_di.WoogiProperty
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
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

class ClassG {
    @WoogiProperty
    lateinit var property1: ClassH
}

data class ClassH(val message: String = "")

class DependencyInjectorTest {

    @Test
    fun `생성자에 인자가 있는 경우 자동으로 의존성 주입을 수행한다`() {
        // given
        val injector = WoogiInjector()

        // when
        val instanceB = injector.inject<ClassB>()
        val actual = instanceB::arg1.returnType.jvmErasure

        // then
        val expected = ClassC::class

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `생성자의 인자의 생성자에 인자가 있는 경우 자동으로 의존성 주입을 수행한다`() {
        // given
        val injector = WoogiInjector()

        // when
        val instanceD = injector.inject<ClassD>()
        val actual1 = instanceD::arg1.returnType.jvmErasure
        val actual2 = instanceD.arg1::arg1.returnType.jvmErasure

        // then
        val expected1 = ClassE::class
        val expected2 = ClassF::class

        assertAll(
            { assertThat(actual1).isEqualTo(expected1) },
            { assertThat(actual2).isEqualTo(expected2) }
        )
    }

    @Test
    fun `생성자의 인자에 WoogiAnnotation이 붙어있는 프로퍼티의 경우 Container로부터 해당 프로퍼티 타입의 인스턴스를 가져온다`() {
        // given
        val container: WoogiContainer = mockk()
        val injector = WoogiInjector(container)

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

    @Test
    fun `생성자의 인자에 WoogiAnnotation이 붙어있는 프로퍼티의 경우 Container에 존재하지 않으면 예외가 발생한다`() {
        // given
        val container: WoogiContainer = mockk()
        val injector = WoogiInjector(container)

        every {
            container.find(ClassB::class)
        } returns null

        // then
        assertThrows<NoSuchElementException> { injector.inject<ClassA>() }
    }

    @Test
    fun `파라미터가 아닌 클래스 내부에 위치한 WoogiAnnotation이 붙어있는 프로퍼티에 대한 의존성 주입을 수행할 수 있다`() {
        // given
        val container: WoogiContainer = mockk()
        val injector = WoogiInjector(container)

        every {
            container.find(ClassH::class)
        } returns ClassH()

        // when
        val instance = injector.inject<ClassG>()
        val actual = instance.property1

        // then
        val expected = ClassH()
        assertEquals(expected, actual)
    }
}
