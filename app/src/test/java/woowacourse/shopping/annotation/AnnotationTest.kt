package woowacourse.shopping.annotation

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.CLASS)
annotation class ClassOnly

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly

@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Source
@Binary
@Runtime
class Pizza(@PropertyOnly val topping: String)

@ClassOnly
@All
class Chicken

class AnnotationTest {

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val clazz = Chicken::class

        assertTrue(clazz.hasAnnotation<All>())
        assertTrue(clazz.hasAnnotation<ClassOnly>())
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping = Pizza("올리브")::topping

        assertTrue(pizzaTopping.hasAnnotation<PropertyOnly>())
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val pizza = Pizza::class

        println(pizza.hasAnnotation<Runtime>())

        assertFalse(pizza.hasAnnotation<Source>())
        assertFalse(pizza.hasAnnotation<Binary>())
        assertTrue(pizza.hasAnnotation<Runtime>())
    }
}
