package woowacourse.shopping.study

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
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

class Person(var firstName: String, val lastName: String, private var age: Int) {
    fun greeting() {}
    private fun fullName() {}
    private fun Int.isAdult() {}

    fun getAge(): Int = age

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

    // 수업 코드
    @Test
    fun `변경 가능한 공개 프로퍼티 값 변경_firstName`() {
        val person = Person("pingu", "An", 20)
        Person::firstName.set(person, "heeae") // Person의 firstName에 접근해서 set()호출
        Assertions.assertEquals("heeae", person.firstName)
    }

    @Test
    fun `변경 불가능한 공개 프로퍼티 값 변경_lastName`() {
        val person = Person("pingu", "An", 20)
        // lastName은 value여서 set이 없어요
        // 읽기전용의 field에 직접 접근해야해서 자바로 해야해
        val field = Person::class.java.getDeclaredField("lastName") // java의 필드에 접근
        // 읽기전용이니까 field는 private으로 선언되어 있어. 그러니 이거를 열어줘야해
        field.isAccessible = true
        field.set(person, "Kim")
        Assertions.assertEquals("Kim", person.lastName)
    }

    @Test
    fun `변경 가능한 비공개 프로퍼티 값 변경_age`() {
        // age
        val person = Person("pingu", "An", 20)
        // 모든 프로퍼티를 들고 와
        // 그리고 mutable(변경 가능)한 프로퍼티들을 가져와
        val property1 = person::class.declaredMemberProperties
            .filterIsInstance<KMutableProperty<*>>()
        // 위에랑 밑이랑 또이또이
        val property2 = person::class.declaredMemberProperties
            .first { it.name == "age" } as KMutableProperty<*>

        // property1.setter.call(person, "30") <- 이것이 되게해보아라(숙제)
        Assertions.assertEquals(30, person.getAge())
    }
}
