package di

import com.boogiwoogi.di.WoogiContainer
import com.boogiwoogi.di.WoogiInjector
import com.boogiwoogi.di.WoogiProperty
import com.boogiwoogi.di.WoogiQualifier
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import kotlin.reflect.jvm.jvmErasure

class DependencyInjectorTest {

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
        assertThat(actual).isEqualTo(expected)
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
        assertThat(actual).isEqualTo(expected)
    }

    interface Repository

    class ExampleRepository : Repository
    class ExampleRepository2 : Repository
    class ExampleViewModel(
        @WoogiQualifier(ExampleRepository::class)
        val repository: Repository
    )

    class ExampleViewModel2(
        @WoogiQualifier(ExampleRepository::class)
        val repository: Repository,
        @WoogiQualifier(ExampleRepository2::class)
        val repository2: Repository
    )

    @Test
    fun `같은 인터페이스에 대한 구현체가 여러개가 있어도 Qualifier 어노테이션 타입으로 의존성 주입이 이루어진다`() {
        // given
        val woogiContainer: WoogiContainer = mockk(relaxed = true)
        val woogiInjector = WoogiInjector(woogiContainer)

        every {
            woogiContainer.find(ExampleRepository::class)
        } returns ExampleRepository()

        // when
        val viewModel = woogiInjector.inject<ExampleViewModel>()
        val actual = viewModel.repository

        // then
        Assertions.assertTrue(actual is ExampleRepository)
        verify { woogiContainer.find(ExampleRepository::class) }
    }

    @Test
    fun `Qualifier에 선언한 타입의 인스턴스가 Container에 없는 경우 예외가 발생한다`() {
        // given
        val woogiContainer: WoogiContainer = mockk(relaxed = true)
        val woogiInjector = WoogiInjector(woogiContainer)

        every {
            woogiContainer.find(ExampleRepository::class)
        } returns null

        // then
        assertThrows<NoSuchElementException> {
            val viewModel = woogiInjector.inject<ExampleViewModel>()
        }
    }

    @Test
    fun `같은 인터페이스에 대한 구현체가 여러개가 있어도 각각의 Qualifier 어노테이션 타입으로 의존성 주입이 이루어진다`() {
        // given
        val woogiContainer: WoogiContainer = mockk(relaxed = true)
        val woogiInjector = WoogiInjector(woogiContainer)

        every {
            woogiContainer.find(ExampleRepository::class)
        } returns ExampleRepository()

        every {
            woogiContainer.find(ExampleRepository2::class)
        } returns ExampleRepository2()

        // when
        val viewModel = woogiInjector.inject<ExampleViewModel2>()
        val actual = viewModel.repository
        val actual2 = viewModel.repository2

        // then
        Assertions.assertTrue(actual is ExampleRepository && actual2 is ExampleRepository2)
        verify { woogiContainer.find(ExampleRepository::class) }
        verify { woogiContainer.find(ExampleRepository2::class) }
    }
}
