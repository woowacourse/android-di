package woowacourse.shopping

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import woowacourse.shopping.di.DIContainer
import java.lang.IllegalArgumentException

class NoParameter

data class OneParameter(private val p1: String)

data class TwoParameter(private val p1: String, private val p2: String)

data class ThreeParameter(private val p1: String, private val p2: String, private val p3: String)

class DIContainerTest {
    private lateinit var diContainer: DIContainer

    @BeforeEach
    fun setUp() {
        diContainer = DIContainer()
    }

    @Test
    fun `파라미터가 없는 클래스의 인스턴스를 생성한다`() {
        // given
        val classType = NoParameter::class
        val parameters: List<Any> = emptyList()

        // when
        val actual = diContainer.get(classType, parameters)

        // then
        assertThat(actual).isInstanceOf(classType.java)
    }

    @Test
    fun `파라미터가 한 개인 클래스의 인스턴스를 생성한다`() {
        // given
        val classType = OneParameter::class
        val parameters: List<Any> = listOf("a")

        // when
        val actual = diContainer.get(classType, parameters)

        // then
        assertThat(actual).isEqualTo(OneParameter("a"))
    }

    @Test
    fun `파라미터가 두 개인 클래스의 인스턴스를 생성한다`() {
        // given
        val classType = TwoParameter::class
        val parameters: List<Any> = listOf("a", "b")

        // when
        val actual = diContainer.get(classType, parameters)

        // then
        assertThat(actual).isEqualTo(TwoParameter("a", "b"))
    }

    @Test
    fun `파라미터가 세 개인 클래스의 인스턴스를 생성한다`() {
        // given
        val classType = ThreeParameter::class
        val parameters: List<Any> = listOf("a", "b", "c")

        // when
        val actual = diContainer.get(classType, parameters)

        // then
        assertThat(actual).isEqualTo(ThreeParameter("a", "b", "c"))
    }

    @Test
    fun `파라미터 한 개로 파라미터가 없는 클래스의 인스턴스를 생성하면 오류가 발생한다`() {
        // given
        val classType = NoParameter::class
        val parameters: List<Any> = listOf("a")

        // when, then
        assertThrows<IllegalArgumentException> {
            diContainer.get(classType, parameters)
        }
    }


    @Test
    fun `파라미터 한 개로 파라미터가 두 개인 클래스의 인스턴스를 생성하면 오류가 발생한다`() {
        // given
        val classType = TwoParameter::class
        val parameters: List<Any> = listOf("a")

        // when, then
        assertThrows<IllegalArgumentException> {
            diContainer.get(classType, parameters)
        }
    }

    @Test
    fun `동일한 클래스의 인스턴스를 두 번 생성하면 기존 인스턴스를 반환한다`() {
        // given
        val classType = NoParameter::class
        val instance = diContainer.get(classType)

        // when
        val actual = diContainer.get(classType)

        // then
        assertThat(actual).isSameInstanceAs(instance)
    }

    @Test
    fun `String 타입인 파라미터에 Int 타입을 넣으면 오류가 발생한다`() {
        // given
        val classType = OneParameter::class
        val parameters: List<Any> = listOf(1)

        // when, then
        assertThrows<IllegalArgumentException> {
            diContainer.get(classType, parameters)
        }
    }
}
