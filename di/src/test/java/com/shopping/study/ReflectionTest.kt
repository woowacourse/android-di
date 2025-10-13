package com.shopping.study

import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.equals.shouldBeEqual
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.staticFunctions
import org.junit.Test

class Person(
    var firstName: String,
    val lastName: String,
    private var age: Int,
) {
    fun greeting() {}

    private fun fullName() {}

    private fun Int.isAdult() {}

    companion object {
        fun noname(age: Int): Person = Person("", "", age)
    }
}

class ReflectionTest {
    @Test
    fun `변경 가능한 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20)
        Person::firstName.set(person, "Jaesung")
        person.firstName shouldBeEqual "Jaesung"
    }

    @Test
    fun `읽기 전용 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20)
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            isAccessible = true
            set(person, "Mraz")
        }
        person.lastName shouldBeEqual "Mraz"
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티`() {
        val declaredMemberProperties = Person::class.declaredMemberProperties
        declaredMemberProperties shouldHaveSize 3
    }

    @Test
    fun `클래스 내에서 선언된 변경 가능한 프로퍼티`() {
        val mutableProperties =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        mutableProperties shouldHaveSize 2
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20)
        val firstNameProperty =
            Person::class
                .declaredMemberProperties
                .filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        person.firstName shouldBeEqual "Jaesung"
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        personReflection.functions shouldHaveSize 6
        // fullName, greeting, equals, hashCode, toString
        personReflection.memberFunctions shouldHaveSize 5
        // isAdult
        personReflection.memberExtensionFunctions shouldHaveSize 1
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult
        personReflection.declaredFunctions shouldHaveSize 3
        // greeting, isAdult
        personReflection.declaredMemberFunctions shouldHaveSize 2
        // isAdult
        personReflection.declaredMemberExtensionFunctions shouldHaveSize 1
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        personReflection.functions shouldHaveSize 6
        // fullName, greeting, isAdult
        personReflection.declaredFunctions shouldHaveSize 3
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        Person::class.staticFunctions shouldHaveSize 0
    }
}
