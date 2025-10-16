package woowacourse.study

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.reflect.full.findAnnotation
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

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Source
@Binary
@Runtime
class Chicken

class AnnotationReflectionTest {
    @Test
    fun `클래스의 어노테이션 존재 여부를 확인한다`() {
        val pizzaClass = Pizza::class

        assertTrue(pizzaClass.hasAnnotation<ClassOnly>())
        assertTrue(pizzaClass.hasAnnotation<All>())
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부를 확인한다`() {
        val pizzaTopping = Pizza("올리브")::topping
        assertTrue(pizzaTopping.hasAnnotation<PropertyOnly>())
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부를 확인한다`() {
        val chickenClass = Chicken::class

        assertFalse(chickenClass.hasAnnotation<Source>())
        assertFalse(chickenClass.hasAnnotation<Binary>())
        assertTrue(chickenClass.hasAnnotation<Runtime>())
    }

    @Test
    fun `어노테이션 속성 값을 가져온다`() {
        val pizzaClass = Pizza::class
        val pizzaClassAnnotation = pizzaClass.findAnnotation<All>()!!

        val annotationClass = pizzaClassAnnotation.annotationClass
        assertTrue(annotationClass == All::class)

        val annotationName = annotationClass.simpleName
        assertEquals(annotationName, "All")
    }
}
