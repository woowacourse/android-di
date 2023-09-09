package woowacourse.shopping.di

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class ArkInjectTest {
    @Test
    fun `DiInject는 Runtime에 유지되어야 한다`() {
        // given
        val annotationRetention = ArkInject::class.java.getAnnotation(Retention::class.java)

        // when
        val value = annotationRetention?.value

        // when & then
        assertThat(value).isEqualTo(AnnotationRetention.RUNTIME)
    }

    @Test
    fun `DiInject는 생성자와 필드에 달 수 있다`() {
        // given
        val annotationTarget = ArkInject::class.java.getAnnotation(Target::class.java)

        // when
        val allowedTargets = annotationTarget?.allowedTargets ?: emptyArray()

        // then
        assertThat(allowedTargets.size).isEqualTo(2)

        // and
        assertThat(allowedTargets.firstOrNull { it == AnnotationTarget.CONSTRUCTOR })
            .isEqualTo(AnnotationTarget.CONSTRUCTOR)
    }
}
