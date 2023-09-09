package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

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

@Target(AnnotationTarget.ANNOTATION_CLASS)
annotation class ParentMetaAnnotation

@ParentMetaAnnotation
@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime(val name: String)

@Source
@Binary
@Runtime("hi")
class Chicken

class AnnotationReflectionTest {

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val pizzaClass = Pizza::class

        assertThat(pizzaClass.hasAnnotation<ClassOnly>()).isTrue
        assertThat(pizzaClass.hasAnnotation<All>()).isTrue
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping = Pizza("올리브")::topping

        assertThat(pizzaTopping.hasAnnotation<PropertyOnly>()).isTrue
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass = Chicken::class

        assertThat(chickenClass.hasAnnotation<Source>()).isFalse
        assertThat(chickenClass.hasAnnotation<Binary>()).isFalse
        assertThat(chickenClass.hasAnnotation<Runtime>()).isTrue
    }

    class TestClass1(
        val name: String,
        val id: String,
    ) {
        @Runtime("test_hi_name")
        lateinit var hi: String
    }

    @Test
    fun `필드 주입 테스트`() {
        val type = TestClass1::class
        val primaryConstructor = type.primaryConstructor ?: throw NullPointerException("주생성자 널")

        val declaredParameters = type.declaredMemberProperties
        println(declaredParameters)
        val filterAnnotationField = declaredParameters.filter { it.hasAnnotation<Runtime>() }
        println(filterAnnotationField)
        val annotation = filterAnnotationField.first().findAnnotation<Runtime>()
            ?: throw NullPointerException("Runtime 어노테이션 붙은 거 없음")
        val nameFromAnnotation = annotation.name
        println(annotation.name)
        println(annotation.annotationClass.qualifiedName)

        annotation::class.hasAnnotation<ParentMetaAnnotation>()
        println(annotation.annotationClass.hasAnnotation<ParentMetaAnnotation>())

        assertThat(filterAnnotationField.size).isEqualTo(1)
        assertThat(nameFromAnnotation).isEqualTo("test_hi_name")
        assertThat(annotation.annotationClass.hasAnnotation<ParentMetaAnnotation>()).isTrue
    }

    @Test
    fun `필드 주입 테스트2`() {
        val type = TestClass1::class
        val primaryConstructor =
            type.primaryConstructor ?: throw java.lang.NullPointerException("널 ")
        val instance = primaryConstructor.call("이름", "번호")
        val injectFields = type.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
            .filter { it.hasAnnotation<Runtime>() }
        injectFields.forEach { field ->
            field.setter.call(instance, "주입되었음")
            val paramKClass = field.returnType.classifier as KClass<*>
            println("필드 타입: $paramKClass")
        }

        assertThat(instance.hi).isEqualTo("주입되었음")
    }
}
