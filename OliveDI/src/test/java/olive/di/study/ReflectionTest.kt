package olive.di.study

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.staticFunctions
import kotlin.reflect.full.superclasses

class Person(var firstName: String, val lastName: String, private var age: Int) {
    fun greeting() {}

    private fun fullName() {}

    private fun Int.isAdult() {}

    companion object {
        fun noname(age: Int): Person = Person("", "", age)
    }
}

interface Engine

class Car : Engine

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

    @Test
    fun `클래스의 부모 클래스를 가져온다1`() {
        val carReflection = Car::class
        val carSuperReflections = carReflection.superclasses.filterNot { it == Any::class }

        assertThat(carSuperReflections).hasSize(1)
        assertThat(carSuperReflections.first()).isEqualTo(Engine::class)
    }

    @Test
    fun `클래스의 부모 클래스를 가져온다2`() {
        val personReflection = Person::class
        val personSuperReflections = personReflection.superclasses.filterNot { it == Any::class }

        assertThat(personSuperReflections).hasSize(0)
    }

    @Test
    fun `인터페이스인지 판단한다1`() {
        val engineReflection = Engine::class
        assertThat(engineReflection.isAbstract).isTrue()
    }

    @Test
    fun `인터페이스인지 판단한다2`() {
        val carReflection = Car::class
        assertThat(carReflection.isAbstract).isFalse()
    }

    @Test
    fun `생성자가 없는 클래스의 경우 기본 생성자가 생성된다`() {
        val carReflection = Car::class
        assertThat(carReflection.constructors).hasSize(1)
    }
}
