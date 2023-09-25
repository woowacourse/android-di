package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.jvm.jvmErasure

annotation class AA
class Person(var firstName: String, val lastName: String, private var age: Int) {
    fun greeting() {}
    private fun fullName() {}
    private fun Int.isAdult() {}
    fun aa() {}

    companion object {
        fun noname(age: Int): Person = Person("", "", age)
    }
}

class ReflectionTest {

    @Test
    fun `변경 가능한 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20)
        Person::firstName.set(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `읽기 전용 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20)
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            // todo: public 인데 왜 isAccessible 설정..?
            isAccessible = true
            set(person, "Mraz")
        }
        assertThat(person.lastName).isEqualTo("Mraz")
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티`() {
        val declaredMemberProperties = Person::class.declaredMemberProperties
        assertThat(declaredMemberProperties.size).isEqualTo(3)
    }

    @Test
    fun `클래스 내에서 선언된 변경 가능한 프로퍼티`() {
        val mutableProperties =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        assertThat(mutableProperties.size).isEqualTo(2)
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20)
        val firstNameProperty =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(6)
        // fullName, greeting, equals, hashCode, toString
        assertThat(personReflection.memberFunctions.size).isEqualTo(5)
        // isAdult
        assertThat(personReflection.memberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult
        assertThat(personReflection.declaredFunctions.size).isEqualTo(3)
        // greeting, isAdult
        assertThat(personReflection.declaredMemberFunctions.size).isEqualTo(2)
        // isAdult
        assertThat(personReflection.declaredMemberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(6)
        // fullName, greeting, isAdult
        assertThat(personReflection.declaredFunctions.size).isEqualTo(3)
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        assertThat(Person::class.staticFunctions.size).isEqualTo(0)
    }

    // 자습
    interface Repository
    class FakeModule {
        val repository: Repository = object : Repository {}
    }

    @Test
    fun `타입으로 클래스의 필드값 가져오기`() {
        val clazz: KClass<Repository> = Repository::class
        val declaredProperties = FakeModule::class.declaredMemberProperties
        val instance = declaredProperties.first { it.returnType.jvmErasure == clazz }
    }

    @Test
    fun `함수의 매개변수 타입 가져오기`() {
        val declaredFunctions = Person::class.declaredFunctions
        val function: KFunction<*> = declaredFunctions.first { it.name == "aa" }
        println(function.parameters)
        function.call(Person::class)
    }
}
