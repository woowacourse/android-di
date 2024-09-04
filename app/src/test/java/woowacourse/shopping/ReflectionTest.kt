package woowacourse.shopping

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

class Person(
    var firstName: String,
    val lastName: String,
    private var age: Int
) {
    fun greeting() {}
    private fun fullName() {}
    private fun Int.isAdult() {}

    companion object {
        fun noName(age: Int): Person = Person("", "", age)
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
    fun `변경 가능한 공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20)
        val firstNameProperty =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        assertThat(person.firstName).isEqualTo("Jaesung")
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20)
        val ageProperty =
            Person::class.java.getDeclaredField("age")
        ageProperty.isAccessible = true
        ageProperty.setInt(person, 30)
        assertThat(ageProperty.getInt(person)).isEqualTo(30)
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        assertThat(personReflection.functions.size).isEqualTo(6)
        assertThat(personReflection.memberFunctions.size).isEqualTo(5)
        assertThat(personReflection.memberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        assertThat(personReflection.declaredFunctions.size).isEqualTo(3)
        assertThat(personReflection.declaredMemberFunctions.size).isEqualTo(2)
        assertThat(personReflection.declaredMemberExtensionFunctions.size).isEqualTo(1)
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        assertThat(personReflection.functions.size).isEqualTo(6)
        assertThat(personReflection.declaredFunctions.size).isEqualTo(3)
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        assertThat(Person::class.staticFunctions.size).isEqualTo(0)
    }
}