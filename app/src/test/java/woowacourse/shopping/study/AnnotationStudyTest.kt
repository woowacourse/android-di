package woowacourse.shopping.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KVisibility
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.jvmErasure

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

@Retention(AnnotationRetention.RUNTIME)
annotation class Runtime

@Source
@Binary
@Runtime
class Chicken

annotation class InjectStudy

@InjectStudy
class Repository(
    val daoParameter: DaoInterface,
) {
    @InjectStudy
    lateinit var daoField: DaoInterface
    lateinit var daoName: String
    val isNameInitialized: Boolean get() = ::daoName.isInitialized
}

@InjectStudy
class Dao : DaoInterface

interface DaoInterface

class AnnotationReflectionTest {

    @Test
    fun `생성자를 주입받은 후, 필드 주입까지 성공하는지 테스트`() {
        val map = mutableMapOf(DaoInterface::class to Dao())

        val repositoryClass = Repository::class
        val constructor = repositoryClass.primaryConstructor
        val parameters = constructor!!.parameters

        val arguments = parameters.associateWith { parameter ->
            map[parameter.type.jvmErasure]
        }

        val repository = constructor.callBy(arguments)

        val properties = repositoryClass.declaredMemberProperties
            .filter { it.visibility == KVisibility.PUBLIC }
            .filterIsInstance<KMutableProperty<*>>()

        properties.forEach { property ->
            if (property.hasAnnotation<InjectStudy>()) {
                property.setter.call(repository, map[property.returnType.jvmErasure])
            }
        }

        assertThat(repository).isNotNull()
        assertThat(repository.daoParameter).isNotNull()
        assertThat(repository.daoField).isNotNull()
        assert(!repository.isNameInitialized)
    }

    @Test
    fun `클래스의 어노테이션 존재 여부`() {
        val pizzaClass = Pizza::class

        assert(pizzaClass.hasAnnotation<ClassOnly>())
        assert(pizzaClass.hasAnnotation<All>())
    }

    @Test
    fun `프로퍼티의 어노테이션 존재 여부`() {
        val pizzaTopping = Pizza("올리브")::topping

        assert(pizzaTopping.hasAnnotation<PropertyOnly>())
    }

    @Test
    fun `어노테이션의 Reflection 접근 가능 여부`() {
        val chickenClass = Chicken::class

        assert(!chickenClass.hasAnnotation<Source>())
        assert(!chickenClass.hasAnnotation<Binary>())
        assert(chickenClass.hasAnnotation<Runtime>())
    }
}
