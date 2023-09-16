package woowacourse.shopping.study

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class OtterPropertyAnnotation

@Target(AnnotationTarget.CLASS)
annotation class OtterClassAnnotation

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@All
@OtterClassAnnotation
class Otter(
    val name: String,
    @OtterPropertyAnnotation private var address: String,
)

@Source
@Binary
@Runtime
class Chicken

class AnnotationStudyTest() {

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val otterClass = Otter::class

        assertTrue(otterClass.hasAnnotation<OtterClassAnnotation>())
        assertTrue(otterClass.hasAnnotation<All>())
    }

    @Test
    fun `클레스의 프로퍼티 중 OtterPropertyAnnotation이 있는 프로퍼티만 가져온다`() {
        val otter = Otter(name = "수달", address = "hello")
        val otterProperties = otter::class.declaredMemberProperties
        val otterAnnotationProperties = otterProperties.filter { it ->
            it.annotations.any { it is OtterPropertyAnnotation }
        }.map { it.name }

        assertTrue(otterAnnotationProperties.contains("address"))
        assertFalse(otterAnnotationProperties.contains("name"))
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass = Chicken::class

        assertFalse(chickenClass.hasAnnotation<Source>())
        assertFalse(chickenClass.hasAnnotation<Binary>())
        assertTrue(chickenClass.hasAnnotation<Runtime>())
    }
}
