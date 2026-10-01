package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties

@Target(AnnotationTarget.PROPERTY)
annotation class MyInject

class AnnotatedPerson(
    var firstName: String,
    val lastName: String,
    private var age: Int,
) {
    @MyInject
    val middleName: String = ""
}

class NoneAnnotatedPerson(
    var firstName: String,
    val lastName: String,
    private var age: Int,
) {
    var middleName: String = ""
}

class AnnotationReflectionTest {
    @Test
    fun `MyInject가 붙은 프로퍼티를 찾는다`() {
        val annotatedPropertyNames =
            AnnotatedPerson::class
                .declaredMemberProperties
                .filter { property -> property.annotations.any { it is MyInject } }
                .map { it.name }

        assertThat(annotatedPropertyNames).containsExactly("middleName")
    }

    @Test
    fun `MyInject가 없는 프로퍼티는 찾지 않는다`() {
        val annotatedPropertyNames =
            NoneAnnotatedPerson::class
                .declaredMemberProperties
                .filter { property -> property.annotations.any { it is MyInject } }
                .map { it.name }

        assertThat(annotatedPropertyNames.isEmpty()).isEqualTo(true)
    }
}
