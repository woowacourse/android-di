package com.ki960213.sheath.component

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.functions
import kotlin.reflect.full.primaryConstructor

internal class SheathComponentTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `SheathComponent의 클래스의 애노테이션이 붙을 수 있는 요소 중 @Inject 애노테이션이 붙은 요소가 없을 때, 주 생성자의 파라미터들 중 어떤 파라미터의 타입이 다른 SheathComponent의 클래스의 서브 타입이라면 의존하는 것이다`() {
        val sheathComponent1 = SheathComponent(Test1::class)
        val sheathComponent2 = SheathComponent(Test2::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Component
    private class Test1(test2: Test2)

    @Component
    private class Test2

    @Test
    fun `SheathComponent의 클래스의 멤버 변수 중 @Inject가 붙은 어떤 멤버 변수의 타입이 다른 SheathComponent의 클래스의 서브 타입이라면 의존하는 것이다`() {
        val sheathComponent1 = SheathComponent(Test3::class)
        val sheathComponent2 = SheathComponent(Test4::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Component
    private class Test3 {
        @Inject
        private lateinit var test4: Test4
    }

    @Component
    private class Test4

    @Test
    fun `SheathComponent의 클래스의 @Inject가 붙은 KCallable 타입 중 매개 변수 중 타입이 다른 SheathComponent의 클래스의 서브 타입이라면 의존하는 것이다`() {
        val sheathComponent1 = SheathComponent(Test5::class)
        val sheathComponent2 = SheathComponent(Test6::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Component
    private class Test5 {
        @Inject
        private fun test(test6: Test6): Unit = Unit
    }

    @Component
    private class Test6

    @Test
    fun `SheathComponent의 클래스의 애노테이션이 붙을 수 있는 요소 중 @Inject 애노테이션이 붙은 요소가 여러 개면 모두 의존하는지 판단하는 대상이다`() {
        val sheathComponent1 = SheathComponent(Test7::class)
        val sheathComponent2 = SheathComponent(Test8::class)
        val sheathComponent3 = SheathComponent(Test9::class)
        val sheathComponent4 = SheathComponent(Test10::class)

        expect.that(sheathComponent1.isDependingOn(sheathComponent2)).isTrue()
        expect.that(sheathComponent1.isDependingOn(sheathComponent3)).isTrue()
        expect.that(sheathComponent1.isDependingOn(sheathComponent4)).isTrue()
    }

    @Component
    private class Test7 @Inject constructor(test8: Test8) {
        @Inject
        private lateinit var test9: Test9

        @Inject
        private fun test(test10: Test10): Unit = Unit
    }

    @Component
    private class Test8

    @Component
    private class Test9

    @Component
    private class Test10

    @Test
    fun `SheathComponent의 클래스에 @Inject가 붙은 요소 중 파라미터 타입이나 프로퍼티의 타입이 다른 SheathComponent의 클래스의 서브 타입이 없으면 의존하지 않는 것이다`() {
        val sheathComponent1 = SheathComponent(Test11::class)
        val sheathComponent2 = SheathComponent(Test15::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isFalse()
    }

    @Component
    private class Test11 @Inject constructor(test12: Test12) {
        @Inject
        private lateinit var test13: Test13

        @Inject
        private fun test(test14: Test14): Unit = Unit
    }

    @Component
    private class Test12

    @Component
    private class Test13

    @Component
    private class Test14

    @Component
    private class Test15

    @Test
    fun `클래스에 여러 개의 생성자에 @Inject 애노테이션이 붙어있다면 생성할 때 에러가 발생한다`() {
        try {
            SheathComponent(Test16::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("여러 개의 생성자에 @Inject 애노테이션을 붙일 수 없습니다.")
        }
    }

    @Component
    private class Test16 @Inject constructor(test17: Test17) {
        @Inject
        constructor(test18: Test18) : this(test18 as Test17)
    }

    @Component
    private open class Test17

    @Component
    private class Test18 : Test17()

    @Test
    fun `@Inject가 붙은 요소가 없고 주 생성자의 매개 변수가 없다면 인스턴스화 할 수 있다`() {
        val sheathComponent = SheathComponent(Test19::class)

        val actual = sheathComponent.instantiated(listOf())

        assertThat(actual).isInstanceOf(Test19::class.java)
    }

    @Component
    class Test19 {
        private lateinit var name: String
    }

    @Test
    fun `@Inject가 붙은 요소가 없고 주 생성자의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 없다면 인스턴스 생성 시 에러가 발생한다`() {
        val sheathComponent = SheathComponent(Test20::class)

        try {
            sheathComponent.instantiated(listOf())
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test20::class} 클래스의 ${Test20::class.primaryConstructor?.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }
    }

    @Component
    class Test20(test19: Test19)

    @Test
    fun `@Inject가 붙은 요소가 없고 주 생성자의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 있다면 인스턴스화 할 수 있다`() {
        val sheathComponent = SheathComponent(Test20::class)

        val actual = sheathComponent.instantiated(listOf(Test19()))

        assertThat(actual).isInstanceOf(Test20::class.java)
    }

    @Test
    fun `생성자에 @Inject가 붙어 있고 매개 변수가 없다면 인스턴스화 할 수 있다`() {
        val sheathComponent = SheathComponent(Test21::class)

        val actual = sheathComponent.instantiated(listOf())

        assertThat(actual).isInstanceOf(Test21::class.java)
    }

    @Component
    class Test21 @Inject constructor()

    @Test
    fun `생성자에 @Inject가 붙어 있고 그 생성자의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 없다면 인스턴스 생성 시 에러가 발생한다`() {
        val sheathComponent = SheathComponent(Test22::class)

        try {
            sheathComponent.instantiated(listOf())
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test22::class} 클래스의 ${Test22::class.primaryConstructor?.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }
    }

    @Component
    class Test22 @Inject constructor(test23: Test23)

    @Component
    class Test23

    @Test
    fun `생성자에 @Inject가 붙어 있고 그 생성자의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 있다면 인스턴스화 할 수 있다`() {
        val sheathComponent = SheathComponent(Test22::class)

        val actual = sheathComponent.instantiated(listOf(Test23()))

        assertThat(actual).isInstanceOf(Test22::class.java)
    }

    @Test
    fun `프로퍼티에 @Inject가 붙어 있고 그 프로퍼티 타입의 인스턴스가 인스턴스 목록에 없다면 인스턴스 생성 시 에러가 발생한다`() {
        val sheathComponent = SheathComponent(Test24::class)

        try {
            sheathComponent.instantiated(listOf())
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test24::class} 클래스의 ${Test24::class.getProperty("test25")?.name}에 할당할 수 있는 종속 항목이 존재하지 않습니다.")
        }
    }

    private fun KClass<*>.getProperty(name: String): KProperty1<*, *>? =
        this.declaredMemberProperties.find { it.name == name }

    @Component
    class Test24 {
        @Inject
        private lateinit var test25: Test25
    }

    @Component
    class Test25

    @Test
    fun `프로퍼티에 @Inject가 붙어 있고 그 프로퍼티 타입의 인스턴스가 인스턴스 목록에 있다면 필드 주입 된 인스턴스를 생성할 수 있다`() {
        val sheathComponent = SheathComponent(Test24::class)
        val injectInstance = Test25()

        val actual = sheathComponent.instantiated(listOf(injectInstance))

        expect.that(actual).isInstanceOf(Test24::class.java)
        val property = Test24::class.getProperty("test25")
        expect.that(property?.getter?.call(actual)).isEqualTo(injectInstance)
    }

    @Test
    fun `메서드에 @Inject가 붙어 있고 그 메서드에 매개 변수가 없다면 인스턴스를 생성할 수 있다`() {
        val sheathComponent = SheathComponent(Test26::class)

        val actual = sheathComponent.instantiated(listOf())

        assertThat(actual).isInstanceOf(Test26::class.java)
    }

    @Component
    class Test26 {
        @Inject
        private fun test(): Unit = Unit
    }

    @Test
    fun `메서드에 @Inject가 붙어 있고 그 메서드의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 없다면 인스턴스 생성 시 에러가 발생한다`() {
        val sheathComponent = SheathComponent(Test27::class)

        try {
            sheathComponent.instantiated(listOf())
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test27::class} 클래스의 ${Test27::class.getFunction("test")?.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }
    }

    private fun KClass<*>.getFunction(name: String): KFunction<*>? =
        this.functions.find { it.name == name }

    @Component
    class Test27 {
        var injected: Boolean = false

        @Inject
        private fun test(test28: Test28) {
            injected = true
        }
    }

    @Component
    class Test28

    @Test
    fun `메서드에 @Inject가 붙어 있고 그 메서드의 매개 변수가 있을 때 인스턴스 목록에 필요한 종속 항목이 있다면 메서드 주입된 인스턴스를 생성할 수 있다`() {
        val sheathComponent = SheathComponent(Test27::class)
        val injectedInstance = Test28()

        val actual = sheathComponent.instantiated(listOf(injectedInstance))

        expect.that(actual).isInstanceOf(Test27::class.java)
        expect.that((actual as Test27).injected).isTrue()
    }

    @Test
    fun `여러 요소에 @Inject가 붙어 있을 때 인스턴스 목록에 필요한 종속 항목이 있다면 모든 의존성이 주입된 인스턴스를 생성할 수 있다`() {
        val sheathComponent = SheathComponent(Test29::class)
        val instance30 = Test30()
        val instance31 = Test31()
        val instance32 = Test32()

        val actual = sheathComponent.instantiated(listOf(instance30, instance31, instance32))

        expect.that(actual).isInstanceOf(Test29::class.java)
        val property = Test29::class.getProperty("test31")
        expect.that(property?.getter?.call(actual)).isEqualTo(instance31)
        expect.that((actual as Test29).injectedByFunction).isTrue()
    }

    @Component
    class Test29 @Inject constructor(test30: Test30) {
        @Inject
        private lateinit var test31: Test31

        var injectedByFunction: Boolean = false

        @Inject
        private fun test(test32: Test32) {
            injectedByFunction = true
        }
    }

    @Component
    class Test30

    @Component
    class Test31

    @Component
    class Test32

    @Test
    fun `SheathComponent는 클래스가 같으면 같다고 판단한다`() {
        val sheathComponent1 = SheathComponent(Test1::class)
        val sheathComponent2 = SheathComponent(Test1::class)

        assertThat(sheathComponent1).isEqualTo(sheathComponent2)
    }

    @Test
    fun `SheathComponent는 클래스가 다르면 다르다고 판단한다`() {
        val sheathComponent1 = SheathComponent(Test1::class)
        val sheathComponent2 = SheathComponent(Test2::class)

        assertThat(sheathComponent1).isNotEqualTo(sheathComponent2)
    }

    @Test
    fun `SheathComponent의 해시 코드는 클래스의 해시 코드와 같다`() {
        val sheathComponent = SheathComponent(Test1::class)

        val actual = sheathComponent.hashCode()

        assertThat(actual).isEqualTo(Test1::class.hashCode())
    }

    @Test
    fun `@Component 애노테이션 혹은 @Component 애노테이션이 붙은 애노테이션이 없는 클래스로 SheathComponent를 생성하면 에러가 발생한다`() {
        try {
            SheathComponent(Test33::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("@Component가 붙은 클래스 혹은 @Component가 붙은 애노테이션이 붙은 클래스로만 SheathComponent를 생성할 수 있습니다.")
        }
    }

    class Test33
}
