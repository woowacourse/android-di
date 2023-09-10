package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.staticFunctions

class Person(
    var firstName: String,
    val lastName: String,
    private var age: Int,
    private val hobby: String,
) {
    fun greeting() {}
    private fun fullName() {}
    private fun Int.isAdult() {}
    fun getHobby() = hobby

    companion object {
        fun noname(age: Int): Person = Person("", "", age, "")
    }
}

class ReflectionTest {

    @Test
    fun `변경 가능한 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20, "골프")
        Person::firstName.set(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `읽기 전용 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20, "골프")
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            isAccessible = true
            set(person, "Mraz")
        }
        assertThat(person.lastName).isEqualTo("Mraz")
    }

    @Test
    fun `읽기 전용 비공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20, "골프")
        val hobbyField = Person::class.java.getDeclaredField("hobby")
        hobbyField.apply {
            isAccessible = true
            set(person, "축구")
        }
        assertThat(person.getHobby()).isEqualTo("축구")
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티`() {
        val declaredMemberProperties = Person::class.declaredMemberProperties
        assertThat(declaredMemberProperties.size).isEqualTo(4)
    }

    @Test
    fun `클래스 내에서 선언된 변경 가능한 프로퍼티`() {
        val mutableProperties =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        assertThat(mutableProperties.size).isEqualTo(2)
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20, "골프")
        val firstNameProperty =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, getHobby, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(7)
        // fullName, greeting, equals, hashCode, toString, getHobby
        assertThat(personReflection.memberFunctions.size).isEqualTo(6)
        // isAdult
        assertThat(personReflection.memberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, getHobby
        assertThat(personReflection.declaredFunctions.size).isEqualTo(4)
        // greeting, isAdult, getHobby
        assertThat(personReflection.declaredMemberFunctions.size).isEqualTo(3)
        // isAdult, getHobby
        assertThat(personReflection.declaredMemberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, getHobby, equals, hashCode, toString
        assertThat(personReflection.functions.size).isEqualTo(7)
        // fullName, greeting, isAdult, getHobby
        assertThat(personReflection.declaredFunctions.size).isEqualTo(4)
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        assertThat(Person::class.staticFunctions.size).isEqualTo(0)
    }
}
