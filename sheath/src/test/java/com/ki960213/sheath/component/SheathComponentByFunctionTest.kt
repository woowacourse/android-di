package com.ki960213.sheath.component

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Test
import kotlin.reflect.KClass

internal class SheathComponentByFunctionTest {

    @Test
    fun `@Module이 붙은 클래스의 함수가 아닌 함수로 생성하면 에러가 발생한다`() {
        try {
            SheathComponentByFunction(Module1::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Module1::test.name} 함수가 정의된 클래스에 @Module이 붙어있어야 합니다.")
        }
    }

    object Module1 {
        fun test(): Unit = Unit
    }

    @Test
    fun `함수로 생성한 SheathComponent의 clazz는 함수의 리턴 타입의 클래스와 같다`() {
        val sheathComponent = SheathComponentByFunction(Module2::test)

        val actual = sheathComponent.clazz

        assertThat(actual.java).isEqualTo(Unit::class.java)
    }

    @Module
    object Module2 {
        fun test(): Unit = Unit
    }

    @Test
    fun `한정자 애노테이션이 붙지 않았다면 이름은 리턴 타입의 클래스의 qualifiedName과 같다`() {
        val sheathComponent = SheathComponentByFunction(Module3::test)

        val actual = sheathComponent.name

        val clazz = Module3::test.returnType.classifier as KClass<*>
        val expect = clazz.qualifiedName
        assertThat(actual).isEqualTo(expect)
    }

    @Module
    object Module3 {

        fun test(): Unit = Unit
    }

    @Test
    fun `한정자 애노테이션이 붙어있다면 이름은 한정자 애노테이션으로 설정된 이름이다`() {
        val sheathComponent = SheathComponentByFunction(Module4::test)

        val actual = sheathComponent.name

        assertThat(actual).isEqualTo("test4")
    }

    @Module
    object Module4 {
        @Qualifier("test4")
        fun test(): Unit = Unit
    }

    @Test
    fun `@Prototype 애노테이션이 붙어있다면 싱글톤이 아니다`() {
        val sheathComponent = SheathComponentByFunction(Module5::test)

        val actual = sheathComponent.isSingleton

        assertThat(actual).isFalse()
    }

    @Module
    object Module5 {
        @Prototype
        fun test(): Unit = Unit
    }

    @Test
    fun `@Prototype 애노테이션이 붙어있지 않다면 싱글톤이다`() {
        val sheathComponent = SheathComponentByFunction(Module6::test)

        val actual = sheathComponent.isSingleton

        assertThat(actual).isTrue()
    }

    @Module
    object Module6 {
        fun test(): Unit = Unit
    }

    @Test
    fun `의존 개수는 함수의 매개 변수의 개수와 같다`() {
        val sheathComponent = SheathComponentByFunction(Module7::test)

        val actual = sheathComponent.dependentCount

        assertThat(actual).isEqualTo(2)
    }

    @Module
    object Module7 {
        fun test(test1: Test1, test2: Test2): Unit = Unit
    }

    @Component
    class Test1

    @Component
    class Test2

    @Test
    fun `매개 변수에 넣을 인자가 모두 있다면 인스턴스화 할 수 있다`() {
        val sheathComponent = SheathComponentByFunction(Module8::test)

        val actual = sheathComponent.instantiated(listOf(Test1(), Test2()))

        assertThat(actual).isInstanceOf(Test3::class.java)
    }

    @Module
    object Module8 {
        fun test(test1: Test1, test2: Test2): Test3 = Test3(test1, test2)
    }

    @Component
    class Test3(test1: Test1, test2: Test2)

    @Test
    fun `매개 변수에 넣을 인자가 모두 있지 않다면 인스턴스화하면 에러가 발생한다`() {
        val sheathComponent = SheathComponentByFunction(Module8::test)

        try {
            sheathComponent.instantiated(listOf())
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Module8::test.name} 함수의 인자로 넣어줄 종속 항목이 존재하지 않습니다.")
        }
    }

    @Test
    fun `함수의 반환 타입이 nullable이면 생성하면 에러가 발생한다`() {
        try {
            SheathComponentByFunction(Module9::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("SheathComponent를 생성할 함수의 리턴 타입이 nullable이면 안됩니다")
        }
    }

    @Module
    object Module9 {
        fun test(): Unit? = Unit
    }

    @Test
    fun `함수에 여러 개의 한정자가 붙어 있다면 생성할 때 에러가 발생한다`() {
        try {
            SheathComponentByFunction(Module10::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("여러 개의 한정자 애노테이션을 붙일 수 없습니다.")
        }
    }

    @Module
    object Module10 {
        @Test10Qualifier
        @Qualifier("test10")
        fun test(): Unit = Unit
    }

    @Qualifier("test10Qualifier")
    private annotation class Test10Qualifier

    @Test
    fun `함수의 매개 변수에 여러 개의 한정자가 붙어 있다면 생성할 때 에러가 발생한다`() {
        try {
            SheathComponentByFunction(Module11::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat().isEqualTo("여러 개의 한정자 애노테이션을 붙일 수 없습니다.")
        }
    }

    @Module
    object Module11 {
        fun test(
            @Test11Qualifier
            @Qualifier("test11")
            test11: Test11,
        ): Unit = Unit
    }

    @Component
    class Test11

    @Qualifier("test11Qualifier")
    private annotation class Test11Qualifier

    @Test
    fun `함수의 매개 변수의 클래스의 타입이 다른 컴포넌트의 클래스의 슈퍼 클래스라면 의존하는 것이다`() {
        val sheathComponent1 = SheathComponentByFunction(Module14::test)
        val sheathComponent2 = SheathComponentByClass(Test15::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Module
    object Module14 {
        fun test(test14: Test14): Unit = Unit
    }

    interface Test14

    @Component
    class Test15 : Test14

    @Test
    fun `함수의 매개 변수의 클래스의 타입이 다른 컴포넌트의 클래스의 슈퍼 클래스가 아니라면 의존하지 않는 것이다`() {
        val sheathComponent1 = SheathComponentByFunction(Module14::test)
        val sheathComponent2 = SheathComponentByClass(Test16::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isFalse()
    }

    @Component
    class Test16

    @Test
    fun `주입 대상에 한정자가 설정되어 있을 때 컴포넌트의 이름이 한정자가 아니라면 의존하지 않는 것이다`() {
        val sheathComponent1 = SheathComponentByFunction(Module12::test)
        val sheathComponent2 = SheathComponentByClass(Test12::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isFalse()
    }

    @Module
    object Module12 {
        fun test(
            @Qualifier("test12")
            test12: Test12,
        ): Unit = Unit
    }

    @Component
    class Test12

    @Test
    fun `주입 대상에 한정자가 설정되어 있을 때 컴포넌트의 이름이 한정자와 같다면 의존하는 것이다`() {
        val sheathComponent1 = SheathComponentByFunction(Module13::test)
        val sheathComponent2 = SheathComponentByClass(Test13::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Module
    object Module13 {
        fun test(
            @Test13Qualifier
            test13: Test13,
        ): Unit = Unit
    }

    @Test13Qualifier
    @Component
    class Test13

    @Qualifier("test13")
    annotation class Test13Qualifier

    @Test
    fun `함수의 매개 변수에 한정자가 설정되어 있지 않다면 그 클래스의 컴포넌트에 설정된 한정자는 의존 여부와 상관 없다`() {
        val sheathComponent1 = SheathComponentByFunction(Module16::test)
        val sheathComponent2 = SheathComponentByClass(Test17::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }

    @Module
    object Module16 {
        fun test(test16: Test17): Unit = Unit
    }

    @Qualifier("test17")
    @Component
    class Test17
}
