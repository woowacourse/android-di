package woowacourse.shopping.di

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

@Sample1
class Fake @Sample2 constructor(
    private val temp: String,
) {
    @Sample3
    private lateinit var temp2: String
}

@Target(AnnotationTarget.CLASS)
annotation class Sample1

@Target(AnnotationTarget.CONSTRUCTOR)
annotation class Sample2

@Target(AnnotationTarget.PROPERTY)
annotation class Sample3

class DependencyInjectorTest {

    @Test
    fun `class 어노테이션 Sample1을 감지한다`() {
        // given :
        val actual = Fake::class.hasAnnotation<Sample1>()
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `constructor 어노테이션 Sample2을 감지한다`() {
        // given :
        val constructor = Fake::class.primaryConstructor
        val actual = constructor?.hasAnnotation<Sample2>()
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `property 어노테이션 Sample3을 감지한다`() {
        // given :
        val constructor = Fake::class.declaredMemberProperties
        val actual = constructor.any {
            it.hasAnnotation<Sample3>()
        }
        // when :
        // then :
        assertEquals(true, actual)
    }
}
