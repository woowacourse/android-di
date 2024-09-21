package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly

@Target(AnnotationTarget.CLASS)
annotation class ClassOnly

@All
@ClassOnly
class Pizza(
    @PropertyOnly val topping: String,
)

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
    fun `Source annotation은 runtime에 접근할 수 없다`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Source>()).isFalse()
    }

    @Test
    fun `Binary annotation은 runtime에 접근할 수 없다`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Binary>()).isFalse()
    }

    @Test
    fun `Runtime annotation은 runtime에 접근할 수 있다`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Runtime>()).isTrue()
    }

    @Test
    fun `Class annotation의 존재 여부`() {
        val pizzaClass = Pizza::class

        assertThat(pizzaClass.hasAnnotation<ClassOnly>()).isTrue
        assertThat(pizzaClass.hasAnnotation<All>()).isTrue
    }

    @Test
    fun `Property annotation의 존재 여부`() {
        val pizzaTopping = Pizza("치즈")::topping

        assertThat(pizzaTopping.hasAnnotation<PropertyOnly>()).isTrue
    }

    @Test
    fun `Property annotation을 사용하여 property를 알 수 있다`() {
        val pizza = Pizza("치즈")
        val topping = pizza::topping.getter.call()

        assertThat(topping).isEqualTo("치즈")
    }
}
