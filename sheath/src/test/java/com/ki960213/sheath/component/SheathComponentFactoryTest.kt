package com.ki960213.sheath.component

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Prototype
import org.junit.Test
import kotlin.reflect.full.createType

class SheathComponentFactoryTest {

    @Test
    fun `클래스로 SheathComponent 객체를 생성하면 타입은 클래스의 타입과 같다`() {
        val actual = SheathComponentFactory.create(Test1::class)

        assertThat(actual.type).isEqualTo(Test1::class.createType())
    }

    @Component
    class Test1

    fun `클래스로 SheathComponent 객체를 생성하면 이름은 클래스의 qualifiedName과 같다`() {
        val actual = SheathComponentFactory.create(Test1::class)

        assertThat(actual.name).isEqualTo(Test1::class.qualifiedName)
    }

    fun `클래스로 SheathComponent 객체를 생성할 때 지역 클래스라면 에러가 발생한다`() {
        @Component
        class Test

        try {
            SheathComponentFactory.create(Test::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("전역적인 클래스로만 SheathComponent를 생성할 수 있습니다.")
        }
    }

    fun `클래스로 SheathComponent 객체를 생성할 때 클래스에 Prototype 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = SheathComponentFactory.create(Test2::class)

        assertThat(actual.isSingleton).isTrue()
    }

    @Prototype
    @Component
    class Test2

    fun `클래스로 SheathComponent 객체를 생성할 때 클래스에 Prototype 애노테이션이 붙은 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = SheathComponentFactory.create(Test3::class)

        assertThat(actual.isSingleton).isTrue()
    }

    @Prototype
    annotation class PrototypeAttached

    @PrototypeAttached
    @Component
    class Test3

    fun `클래스로 SheathComponent 객체를 생성할 때 클래스에 Prototype 애노테이션이 붙은 애노테이션과 Prototype 애노테이션이 붙어 있지 않다면 싱글톤이 아니다`() {
        val actual = SheathComponentFactory.create(Test4::class)

        assertThat(actual.isSingleton).isFalse()
    }

    @Component
    class Test4

    fun `함수로 SheathComponent 객체를 생성하면 타입은 함수의 리턴 타입과 같다`() {
        val actual = SheathComponentFactory.create(Module1::test)

        assertThat(actual.type).isEqualTo(Test5::class.createType())
    }

    @Module
    object Module1 {
        @Component
        fun test(): Test5 = Test5()
    }

    class Test5

    fun `함수로 SheathComponent 객체를 생성하면 이름은 함수의 리턴 타입 클래스의 qualifiedName과 같다`() {
        val actual = SheathComponentFactory.create(Module1::test)

        assertThat(actual.name).isEqualTo(Test5::class.qualifiedName)
    }

    fun `함수로 SheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = SheathComponentFactory.create(Module2::test)

        assertThat(actual.isSingleton).isTrue()
    }

    @Module
    object Module2 {
        @Prototype
        @Component
        fun test(): Unit = Unit
    }

    fun `함수로 SheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙은 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = SheathComponentFactory.create(Module3::test)

        assertThat(actual.isSingleton).isTrue()
    }

    @Module
    object Module3 {
        @PrototypeAttached
        @Component
        fun test(): Unit = Unit
    }

    fun `함수로 SheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙은 애노테이션과 Prototype 애노테이션이 붙어 있지 않다면 싱글톤이 아니다`() {
        val actual = SheathComponentFactory.create(Module4::test)

        assertThat(actual.isSingleton).isFalse()
    }

    @Module
    object Module4 {
        @Component
        fun test(): Unit = Unit
    }
}
