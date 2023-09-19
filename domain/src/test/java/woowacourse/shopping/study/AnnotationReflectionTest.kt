package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
@Repeatable // 같은 어노테이션을 여러개 붙일 수 있게 해줌
annotation class BBOTTO

@BBOTTO
class Movie(@BBOTTO val title: String) {
    @BBOTTO val price: Int = 0
}

@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.RUNTIME)
annotation class RUNTIME

@Retention(AnnotationRetention.BINARY)
annotation class BINARY

@Source
@RUNTIME
@BINARY
class Crew

class AnnotationReflectionTest {
    @Test
    fun `Class 어노테이션이 존재하는지 Reflection을 통해 확인할 수 있다`() {
        val hasAnnotation = Movie::class.hasAnnotation<BBOTTO>()

        assertThat(hasAnnotation).isTrue
    }

    @Test
    fun `Property 어노테이션이 존재하는지 Reflection을 통해 확인할 수 있다`() {
        val titleHasAnnotation = Movie::title.hasAnnotation<BBOTTO>()
        val priceHasAnnotation = Movie::price.hasAnnotation<BBOTTO>()

        assertThat(titleHasAnnotation).isTrue
        assertThat(priceHasAnnotation).isTrue
    }

    @Test
    fun `어노테이션 Reflection 검증`() {
        val hasSourceAnnotation = Crew::class.hasAnnotation<Source>()
        val hasBinaryAnnotation = Crew::class.hasAnnotation<BINARY>()
        val hasRuntimeAnnotation = Crew::class.hasAnnotation<RUNTIME>()

        assertThat(hasSourceAnnotation).isTrue
        assertThat(hasBinaryAnnotation).isTrue
        assertThat(hasRuntimeAnnotation).isTrue
    }
}
