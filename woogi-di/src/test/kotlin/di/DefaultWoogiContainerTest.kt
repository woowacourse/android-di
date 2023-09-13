package di

import com.boogiwoogi.di.DefaultWoogiContainer
import com.boogiwoogi.di.Dependency
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class DefaultWoogiContainerTest {

    interface Alphabet
    class A : Alphabet
    class B
    class C

    @Test
    fun `타입과 일치하는 컨테이너 내부에 존재하는 인스턴스를 찾아서 반환한다`() {
        // given
        val container = DefaultWoogiContainer(
            values = listOf(
                Dependency(A()),
                Dependency(B()),
                Dependency(C())
            )
        )

        // when
        val actual = container.find(A::class)

        // then
        assertTrue(actual is A)
    }

    @Test
    fun `컨테이너 내부에 존재하는 인스턴스중 타입과 일치하는 인스턴스가 없다면 null을 반환한다`() {
        // given
        val container = DefaultWoogiContainer(
            values = listOf(
                Dependency(A()),
                Dependency(B()),
            )
        )

        // when
        val actual = container.find(C::class)

        // then
        assertNull(actual)
    }
}
