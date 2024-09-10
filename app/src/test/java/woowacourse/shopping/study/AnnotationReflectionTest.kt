package woowacourse.shopping.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly

@Target(AnnotationTarget.CLASS)
annotation class ClassOnly

@All
@ClassOnly
class Pizza(@PropertyOnly val topping: String)

@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Source
@Binary
@Runtime
class Chicken

class AnnotationReflectionTest {

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val pizzaClass = Pizza::class

        assertThat(pizzaClass.hasAnnotation<ClassOnly>()).isTrue()
        assertThat(pizzaClass.hasAnnotation<All>()).isTrue()
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping = Pizza("올리브")::topping

        assertThat(pizzaTopping.hasAnnotation<PropertyOnly>()).isTrue()
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Source>()).isFalse()
        assertThat(chickenClass.hasAnnotation<Binary>()).isFalse()
        assertThat(chickenClass.hasAnnotation<Runtime>()).isTrue()
    }
}
