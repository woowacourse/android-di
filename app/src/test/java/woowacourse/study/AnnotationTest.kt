package woowacourse.study

import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.full.hasAnnotation

class AnnotationReflectionTest {

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

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val pizzaClass: KClass<Pizza> = Pizza::class

        pizzaClass.hasAnnotation<ClassOnly>().shouldBeTrue()
        pizzaClass.hasAnnotation<All>().shouldBeTrue()
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping: KProperty0<String> = Pizza("올리브")::topping

        pizzaTopping.hasAnnotation<PropertyOnly>().shouldBeTrue()
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass: KClass<Chicken> = Chicken::class

        chickenClass.hasAnnotation<Source>().shouldBeFalse()
        chickenClass.hasAnnotation<Binary>().shouldBeFalse()
        chickenClass.hasAnnotation<Runtime>().shouldBeTrue()
    }
}
