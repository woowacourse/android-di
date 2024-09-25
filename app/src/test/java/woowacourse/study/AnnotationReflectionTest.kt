package woowacourse.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.jupiter.api.assertAll
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.full.hasAnnotation

annotation class All

@Target(AnnotationTarget.PROPERTY)
annotation class PropertyOnly

@Target(AnnotationTarget.CLASS)
annotation class ClassOnly

// Retention : 애노테이션의 라이프 사이클 즉, 애노테이션이 언제까지 살아 남아 있을지를 정하는 것
@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@All
@ClassOnly
class Pasta(
    @PropertyOnly
    val ingredient: String,
)

@Binary
@Source
@Runtime
class Chicken

class AnnotationReflectionTest {
    @Test
    fun `Pasta Class는 All과 ClassOnly annotation을 가지고 있다`() {
        val pasta: KClass<Pasta> = Pasta::class

        assertAll(
            { assertThat(pasta.hasAnnotation<All>()).isTrue() },
            { assertThat(pasta.hasAnnotation<ClassOnly>()).isTrue() },
        )
    }

    @Test
    fun `Pasta Class의 ingredient는 PropertyOnly annotation을 가지고 있다`() {
        val pasta: KProperty0<String> = Pasta("베이컨")::ingredient

        assertThat(pasta.hasAnnotation<PropertyOnly>()).isTrue()
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chicken: KClass<Chicken> = Chicken::class

        assertAll(
            { assertThat(chicken.hasAnnotation<Binary>()).isFalse() },
            { assertThat(chicken.hasAnnotation<Source>()).isFalse() },
            { assertThat(chicken.hasAnnotation<Runtime>()).isTrue() },
        )
    }
}
