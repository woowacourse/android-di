package com.on.di_library.study

import io.kotest.matchers.shouldBe
import org.junit.Test
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberExtensionFunctions
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberExtensionFunctions
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.staticFunctions

annotation class Hi

abstract class Woowacourse {
    protected val buildingAddress: String = "Seolleung"
}

class Job(
    val name: String,
    val money: Int,
)

class Person(var firstName: String, val lastName: String, private var age: Int) : Woowacourse() {
    val nickName: String = "Jerry"

    @Hi
    private val address: String = "Anyang"

    private lateinit var job: Job

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
        (person.firstName) shouldBe ("Jaesung")
    }

    @Test
    fun `읽기 전용 공개 프로퍼티 값 변경`() {
        val person = Person("Jason", "Park", 20)
        val lastNameField = Person::class.java.getDeclaredField("lastName")
        lastNameField.apply {
            isAccessible = true
            set(person, "Mraz")
        }
        (person.lastName) shouldBe ("Mraz")
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티`() {
        val declaredMemberProperties = Person::class.declaredMemberProperties

        declaredMemberProperties.forEach { properties: KProperty1<out Any, *> ->
            println("parameters : ${properties.parameters}")
            println("name : ${properties.name}")
            println("annotations : ${properties.annotations}")
            println("isLateinit : ${properties.isLateinit}")
            println("returnType: ${properties.returnType}")
            println("-------------------")
        }
        (declaredMemberProperties.size) shouldBe (6)
    }

    @Test
    fun `클래스 내에서 선언된 프로퍼티(상속된 거 포함)`() {
        val declaredMemberProperties = Person::class.memberProperties
        (declaredMemberProperties.size) shouldBe (7)
    }

    @Test
    fun `클래스 내에서 선언된 변경 가능한 프로퍼티`() {
        val mutableProperties =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
        (mutableProperties.size) shouldBe (3)
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 변경`() {
        val person = Person("Jason", "Park", 20)
        val firstNameProperty =
            Person::class.declaredMemberProperties.filterIsInstance<KMutableProperty<*>>()
                .first { it.name == "firstName" }
        firstNameProperty.setter.call(person, "Jaesung")
        (person.firstName) shouldBe ("Jaesung")
    }

    @Test
    fun `클래스 및 부모 클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        (personReflection.functions.size) shouldBe (6)
        // fullName, greeting, equals, hashCode, toString
        (personReflection.memberFunctions.size) shouldBe (5)
        // isAdult
        (personReflection.memberExtensionFunctions.size) shouldBe (1)
    }

    @Test
    fun `클래스 내에서 선언된 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult
        (personReflection.declaredFunctions.size) shouldBe (3)
        // greeting, isAdult
        (personReflection.declaredMemberFunctions.size) shouldBe (2)
        // isAdult
        (personReflection.declaredMemberExtensionFunctions.size) shouldBe (1)
    }

    @Test
    fun `멤버 함수 확장 함수 클래스 내에서 선언된 정적 함수`() {
        val personReflection = Person::class
        // fullName, greeting, isAdult, equals, hashCode, toString
        (personReflection.functions.size) shouldBe (6)
        // fullName, greeting, isAdult
        (personReflection.declaredFunctions.size) shouldBe (3)
    }

    @Test
    fun `클래스 내에서 선언된 정적 함수`() {
        (Person::class.staticFunctions.size) shouldBe (0)
    }
}
