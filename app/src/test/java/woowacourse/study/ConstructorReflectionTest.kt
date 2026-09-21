package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class ConstructorReflectionTest {
    @Test
    fun `생성자의 파라미터 타입을 알아낸다`() {
        val constructor = Person::class.primaryConstructor!!
        val parameterTypes = constructor.parameters.map { it.type.classifier as KClass<*> }

        assertThat(parameterTypes).containsExactly(String::class, String::class, Int::class).inOrder()
    }

    @Test
    fun `알아낸 생성자로 인스턴스를 만든다`() {
        val constructor = Person::class.primaryConstructor!!

        val person = constructor.call("Jason", "Park", 20)

        assertThat(person.firstName).isEqualTo("Jason")
    }
}
