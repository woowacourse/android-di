package woowacourse.shopping.di

import org.junit.Test
import woowacouse.shopping.di.Container
import kotlin.test.assertEquals

class ContainerTest {
    interface SampleType

    class ImplA : SampleType

    class ImplB : SampleType

    @Test
    fun `Qualifier에 따라 올바른 인스턴스를 반환한다`() {
        val container = Container()
        val a = ImplA()
        val b = ImplB()

        container.registerInstances(SampleType::class, a, "A")
        container.registerInstances(SampleType::class, b, "B")

        val resultA = container.get(SampleType::class, "A")
        val resultB = container.get(SampleType::class, "B")

        assertEquals(a, resultA)
        assertEquals(b, resultB)
    }
}
