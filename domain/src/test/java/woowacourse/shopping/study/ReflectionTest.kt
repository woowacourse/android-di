package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.full.valueParameters

class Person(
    var firstName: String,
    val lastName: String,
    private var age: Int,
    private val numberOfFamily: Int,
    private val repo: Repo,
) {
    fun greeting(a: Int, c: Long = 1L) {
        val b: Boolean
    }

    private fun fullName() {}
    private fun Int.isAdult() {}
    private fun Person.haha() {}

    companion object {
        fun noname(age: Int): Person = Person("", "", age, 5, DefaultRepo())
    }
}

open class TestPeople
class TestChild : TestPeople()

interface Repo {
    fun getFriends(): List<Person>
}

class DefaultRepo : Repo {
    override fun getFriends(): List<Person> {
        return listOf()
    }
}

class ReflectionTest {

    @Test
    fun `변경 가능한 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20, 5, DefaultRepo())
        Person::firstName.set(person, "Jaesung")
        person.javaClass.kotlin.java
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `읽기 전용 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20, 5, DefaultRepo())
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            isAccessible = true
            set(person, "Mraz")
        }
        assertThat(person.lastName).isEqualTo("Mraz")
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티`() {
        val declaredMemberProperties = Person::class.declaredMemberProperties
        assertThat(declaredMemberProperties.size).isEqualTo(5)
    }

    @Test
    fun `클래스 내에서 선언된 변경 가능한 프로퍼티`() {
        val mutableProperties =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        assertThat(mutableProperties.size).isEqualTo(2)
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20, 5, DefaultRepo())
        val firstNameProperty =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, haha, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(7)
        // fullName, greeting, equals, hashCode, toString
        assertThat(personReflection.memberFunctions.size).isEqualTo(5)
        // isAdult, haha
        assertThat(personReflection.memberExtensionFunctions.size).isEqualTo(2)
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection: KClass<Person> = Person::class
        println(personReflection.declaredMemberProperties)
        // fullName, greeting, isAdult, haha
        assertThat(personReflection.declaredFunctions.size).isEqualTo(4)
        // fullName, greeting
        println(personReflection)
        assertThat(personReflection.declaredMemberFunctions.size).isEqualTo(2)
        // isAdult, haha
        assertThat(personReflection.declaredMemberExtensionFunctions.size).isEqualTo(2)
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, haha, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(7)
        // fullName, greeting, isAdult, haha
        assertThat(personReflection.declaredFunctions.size).isEqualTo(4)
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        assertThat(Person::class.staticFunctions.size).isEqualTo(0)
    }

    @Test
    fun `클래스의 주 생성자 추출`() {
        val personReflection = Person::class
        val repoReflection = DefaultRepo::class
        println()
//        personReflection.primaryConstructor?.parameters?.forEach {
//            println(it.javaClass.kotlin.primaryConstructor?.parameters?.size)
//        }
        Person::class.java.kotlin
        println(personReflection.primaryConstructor?.call("a", "b", 3, 3, DefaultRepo())?.firstName)
        println(getAnything(Person::class.java).firstName)
        println()
        getAnything2(DefaultRepo::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T : Any> getAnything(modelClass: Class<T>): T {
        val reflectionPrimaryConstructor =
            modelClass.kotlin.primaryConstructor ?: throw RuntimeException("ㅋㅋㅋㅋㅋ")
        reflectionPrimaryConstructor.parameters.forEach {
            val parameterType = it.type::class.java
            println(modelClass.isAssignableFrom(parameterType))
            Person::class.starProjectedType
        }
        val returnTypes = modelClass.kotlin.memberFunctions.map { it.returnType }
        return reflectionPrimaryConstructor.call("a", "b", 3, 3, DefaultRepo()) as T
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getAnything2(modelClass: Class<T>) {
        Person::class.primaryConstructor?.parameters?.forEach {
            val parameterTypeClass = it.type.classifier as KClass<*>
            println()
            println(it.type)
            println(parameterTypeClass)
            println(modelClass)
            println(modelClass.kotlin.isSubclassOf(parameterTypeClass))
            Person::class.starProjectedType
        }
        val func = Person::greeting
        println(func.parameters.map { it.type })
        println(func.valueParameters.map { it.type })
        println(func.valueParameters.map { it.isOptional })
        Person("a", "b", 3, 3, DefaultRepo())::class.memberFunctions.forEach {
            println("${it.name}, $it")
        }

        println("영차")
        println(TestChild::class.isSubclassOf(TestChild::class))
        println(TestChild::class.isSubclassOf(TestPeople::class))
    }
}
