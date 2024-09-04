package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import woowacourse.shopping.di.DIContainer
import java.lang.IllegalArgumentException

class DIContainerTest {
    private lateinit var diContainer: DIContainer

    @BeforeEach
    fun setUp() {
        diContainer = DIContainer()
    }

    @Test
    fun `클래스의 인스턴스를 저장한다`() {
        // given
        val expected = TestClass()

        // when
        diContainer.put(TestClass::class, expected)

        // then
        val actual = diContainer.get(TestClass::class)
        assertThat(actual).isSameInstanceAs(expected)
    }

    @Test
    fun `동일한 클래스의 인스턴스를 두 번 저장하면 오류가 발생한다`() {
        // given
        diContainer.put(TestClass::class, TestClass())

        // when, then
        assertThrows<IllegalArgumentException> {
            diContainer.put(TestClass::class, TestClass())
        }
    }

    @Test
    fun `생성자 파라미터가 없는 인스턴스를 가져온다`() {
        // when
        val actual = diContainer.get(NoParameter::class)

        // then
        assertThat(actual).isInstanceOf(NoParameter::class.java)
    }

    @Test
    fun `생성자 파라미터가 있는 인스턴스를 가져온다`() {
        // given
        diContainer.put(String::class, "olive")

        // when
        val actual = diContainer.get(OneParameter::class)

        // then
        assertThat(actual).isEqualTo(OneParameter("olive"))
    }

    @Test
    fun `생성자 파라미터가 저장되지 않은 인스턴스를 가져오면 오류가 발생한다`() {
        // when, then
        assertThrows<IllegalArgumentException> {
            diContainer.get(OneParameter::class)
        }
    }

    @Test
    fun `동일한 클래스의 인스턴스를 두 번 가져오면 동일한 인스턴스를 반환한다`() {
        // given
        diContainer.put(String::class, "olive")
        val expected = diContainer.get(OneParameter::class)

        // when
        val actual = diContainer.get(OneParameter::class)

        // then
        assertThat(actual).isSameInstanceAs(expected)
    }
}

class TestClass

class NoParameter

data class OneParameter(private val p1: String)
