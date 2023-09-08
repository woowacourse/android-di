package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.full.hasAnnotation

// 제한걸기
@Target(AnnotationTarget.CLASS, AnnotationTarget.PROPERTY)
annotation class Fancy

@Fancy
class Movie(@Fancy val title: String) {
    @Fancy
    val price: Int = 0
}

// javax Inject

// SOURCE, RUNTIME, BINARY
// 컴파일 된 파일에 저장할 것인가 리플렉션으로 꺼내올 수 있게 만들 것인가 default = runtime

@Retention(AnnotationRetention.SOURCE)
annotation class Source

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Source
@Binary
@Runtime
class Crew

class AnnotationReflectionTest {

    @Test
    fun `특정 어노테이션이 존재하는지 reflection 을 통해 확인할 수 있다`() {
        val hasAnnotation = Movie::class.hasAnnotation<Fancy>()
        assertThat(hasAnnotation).isTrue
    }

    @Test
    fun `어노테이션 reflection 검증`() {
        val titleHasAnnotation = Movie::title.hasAnnotation<Fancy>()
        val priceHasAnnotation = Movie::price.hasAnnotation<Fancy>()
        assertThat(titleHasAnnotation).isTrue
        assertThat(priceHasAnnotation).isTrue
    }

    @Test
    fun `특정 어노테이션이 존재하는지 reflection 을 통해 확인할 수 있다2`() {
        val hasSourceAnnotation = Crew::class.hasAnnotation<Source>()
        val hasBinaryAnnotation = Crew::class.hasAnnotation<Binary>()
        val hasRuntimeAnnotation = Crew::class.hasAnnotation<Runtime>()
        assertThat(hasSourceAnnotation).isFalse
        assertThat(hasBinaryAnnotation).isFalse
        assertThat(hasRuntimeAnnotation).isTrue
    }
}
