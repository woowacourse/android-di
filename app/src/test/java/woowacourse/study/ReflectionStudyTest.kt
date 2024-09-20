package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class Dog(
    private val name: String,
    private val age: Int,
) : Animal {
    private val speed: Int = 10
}

interface Animal

class ReflectionStudy {
    @Test
    fun `declaredMemberProperties는 클래스에 직접 선언된 프로퍼티들을 반환한다`() {
        // given
        val alsongDog = Dog("alsong", 5)

        // when
        val actual = alsongDog::class.declaredMemberProperties.map { it.name }

        // then
        assertThat(actual).isEqualTo(listOf("age", "name", "speed"))
    }

    @Test
    fun `primaryConstructer-parameters는 생성자 파라미터들을 반환한다`() {
        // given
        val alsongDog = Dog("alsong", 5)

        // when
        val actual = alsongDog::class.primaryConstructor?.parameters?.map { it.name }

        // then
        assertThat(actual).isEqualTo(listOf("name", "age"))
    }

    @Test
    fun `KClass의 isAbstract 프로퍼티는 KClass가 인터페이스일 때 true를 반환한다`() {
        // given
        val expected = Animal::class.isAbstract

        // then
        assertThat(expected).isTrue()
    }
}
