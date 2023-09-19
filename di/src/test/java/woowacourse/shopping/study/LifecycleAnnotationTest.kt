package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation

@Target(AnnotationTarget.ANNOTATION_CLASS)
internal annotation class LifecycleAnnotation

@LifecycleAnnotation
@Target(AnnotationTarget.CLASS)
internal annotation class ApplicationLifecycleAnnotation

@LifecycleAnnotation
@Target(AnnotationTarget.CLASS)
internal annotation class ActivityLifecycleAnnotation

@ApplicationLifecycleAnnotation
class ApplicationLifecycleClass

@ActivityLifecycleAnnotation
class ActivityLifecycleClass

fun <T : Any> hasApplicationLifecycle(clazz: KClass<T>): Boolean {
    val annotations = clazz.annotations
    val lifecycleAnnotation =
        annotations.firstOrNull { it.annotationClass.hasAnnotation<LifecycleAnnotation>() }
            ?: return false
    return when (lifecycleAnnotation.annotationClass) {
        ApplicationLifecycleAnnotation::class -> true
        ActivityLifecycleAnnotation::class -> false
        else -> false
    }
}

class LifecycleAnnotationTest {

    @Test
    fun `ApplicationLifecycleClass는 ApplicationLifecycleAnnotation을 가진다`() {
        // given
        val clazz = ApplicationLifecycleClass::class

        // when
        val hasApplicationLifecycle = hasApplicationLifecycle(clazz)

        // then
        assertThat(hasApplicationLifecycle).isTrue
    }

    @Test
    fun `ActivityLifecycleClass는 ApplicationLifecycleAnnotation을 어노테이션을 가지지 않는다`() {
        // given
        val clazz = ActivityLifecycleClass::class

        // when
        val hasApplicationLifecycle = hasApplicationLifecycle(clazz)

        // then
        assertThat(hasApplicationLifecycle).isFalse
    }
}
