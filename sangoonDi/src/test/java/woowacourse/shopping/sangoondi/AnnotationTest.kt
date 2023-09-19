package woowacourse.shopping.sangoondi

import junit.framework.TestCase.assertEquals
import org.junit.Test
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class AnnotationTest {

    @Test
    fun `class 어노테이션 Sample1을 인식한다`() {
        // given :
        val actual = Fake::class.hasAnnotation<Sample1>()
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `constructor 어노테이션 Sample2을 인식한다`() {
        // given :
        val constructor = Fake::class.primaryConstructor
        val actual = constructor?.hasAnnotation<Sample2>()
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `property 어노테이션 Sample3을 인식한다`() {
        // given :
        val constructor = Fake::class.declaredMemberProperties
        val actual = constructor.any {
            it.hasAnnotation<Sample3>()
        }
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `function 어노테이션 Sample4을 인식한다`() {
        // given :
        val constructor = Fake::class.declaredFunctions
        val actual = constructor.any {
            it.hasAnnotation<Sample4>()
        }
        // when :
        // then :
        assertEquals(true, actual)
    }

    @Test
    fun `Fake2클래스의 어노테이션을 통해 부모 어노테이션을 인지한다`() {
        // given :
        val actual = Fake2::class.annotations.any { it.annotationClass.hasAnnotation<Sample1>() }

        // when :
        // then :
        assertEquals(true, actual)
    }

    @Sample1
    class Fake @Sample2 constructor(
        private val temp: String,
    ) {
        @Sample3
        private lateinit var temp2: String

        @Sample4
        fun temp3() {
        }
    }

    @Sample5
    class Fake2

    @Target(AnnotationTarget.CLASS)
    annotation class Sample1

    @Target(AnnotationTarget.CONSTRUCTOR)
    annotation class Sample2

    @Target(AnnotationTarget.PROPERTY)
    annotation class Sample3

    @Target(AnnotationTarget.FUNCTION)
    annotation class Sample4

    @Sample1
    annotation class Sample5
}
