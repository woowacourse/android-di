package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.SoftAssertions.assertSoftly
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly

@Target(AnnotationTarget.CLASS)
annotation class ClassOnly

@Target(AnnotationTarget.FIELD)
annotation class FieldOnly

@All
@ClassOnly
class Pizza(
    @PropertyOnly val topping: String,
)

@All
@ClassOnly
class Kimbab {
    @FieldOnly
    val topping: String = "김치"
}

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
        val pizzaClass: KClass<Pizza> = Pizza::class

        assertThat(pizzaClass.hasAnnotation<ClassOnly>()).isTrue
        assertThat(pizzaClass.hasAnnotation<All>()).isTrue
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping: KProperty0<String> = Pizza("올리브")::topping

        assertThat(pizzaTopping.hasAnnotation<PropertyOnly>()).isTrue
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass: KClass<Chicken> = Chicken::class

        assertThat(chickenClass.hasAnnotation<Source>()).isFalse
        assertThat(chickenClass.hasAnnotation<Binary>()).isFalse
        assertThat(chickenClass.hasAnnotation<Runtime>()).isTrue
    }

    @Test
    fun `Field 어노테이션 테스트`() {
        val kimbabTopping: KProperty0<String> = Kimbab()::topping

        assertSoftly {
            assertThat(kimbabTopping.hasAnnotation<FieldOnly>()).isTrue
            assertThat(kimbabTopping.isLateinit).isFalse()
        }
    }
}
