package woowacourse.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
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

@Retention(AnnotationRetention.BINARY)
annotation class Binary

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Source
@Binary
@Runtime
class Chicken

class AnnotationReflectionTest {
    @Test
    fun `클래스의_어노테이션_존재_여부`() {
        val pizzaClass = Pizza::class

        assertThat(pizzaClass.hasAnnotation<ClassOnly>()).isTrue
        assertThat(pizzaClass.hasAnnotation<All>()).isTrue
    }

    @Test
    fun `프로퍼티의_어노테이션_존재_여부`() {
        val pizzaTopping = Pizza("올리브")::topping

        assertThat(pizzaTopping.hasAnnotation<PropertyOnly>()).isTrue
    }

    @Test
    fun `어노테이션의_Reflection_접근_가능_여부`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Source>()).isFalse
        assertThat(chickenClass.hasAnnotation<Binary>()).isFalse
        assertThat(chickenClass.hasAnnotation<Runtime>()).isTrue
    }

    @Test
    fun `Retention_정책에_따라_리플렉션_접근_가능_여부가_다름`() {
        assertThat(Chicken::class.annotations.map { it.annotationClass.simpleName })
            .contains("Runtime")
            .doesNotContain("Source", "Binary")
    }

    @Test
    fun `어노테이션_메타정보_조회`() {
        val meta = PropertyOnly::class.annotations

        val target = meta.filterIsInstance<Target>().first()
        val retention = meta.filterIsInstance<Retention>().firstOrNull()

        assertThat(target.allowedTargets).contains(AnnotationTarget.PROPERTY)
        assertThat(retention?.value).isNull()
    }

    @Test
    fun `클래스에_선언된_모든_어노테이션_조회`() {
        val annotations = Pizza::class.annotations.map { it.annotationClass.simpleName }

        assertThat(annotations).containsExactlyInAnyOrder("All", "ClassOnly")
    }
}
