package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.primaryConstructor

class Dog(
    private val name: String,
    private val age: Int,
) {
    private val speed: Int = 10
}

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
}
