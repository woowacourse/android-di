package com.ki960213.sheath.component

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Test

internal class SheathComponentValidatorTest {

    @Test
    fun `클래스에 Component 애노테이션이 붙어있지 않고 Component 애노테이션이 붙은 애노테이션이 붙어 있지 않다면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Test1::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("클래스에 @Component 혹은 @Component가 붙은 애노테이션이 붙어 있지 않다면 SheathComponent를 생성할 수 없습니다.")
        }
    }

    class Test1

    @Test
    fun `클래스에 @Inject가 여러 생성자에 붙어 있다면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Test2::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("여러 개의 생성자에 @Inject 애노테이션을 붙일 수 없습니다.")
        }
    }

    @Component
    class Test2 @Inject constructor() {
        @Inject
        constructor(any: Any) : this()
    }

    @Test
    fun `클래스의 의존 타입 중 같은 타입이 있다면 한정자가 있어도 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Test3::class)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Test3::class.qualifiedName} 클래스는 같은 타입을 여러 곳에서 의존하고 있습니다.")
        }
    }

    @Component
    class Test3(@Qualifier(Test5::class) test5: Test4) {
        @Inject
        @Qualifier(Test6::class)
        private lateinit var test6: Test4
    }

    interface Test4

    @Component
    class Test5 : Test4

    @Component
    class Test6 : Test4

    @Test
    fun `함수에 Component 애노테이션이 붙어있지 않고 Component 애노테이션이 붙은 애노테이션이 붙어 있지 않다면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Module1::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("함수에 @Component 혹은 @Component가 붙은 애노테이션이 붙어 있지 않다면 SheathComponent를 생성할 수 없습니다.")
        }
    }

    @Module
    object Module1 {
        fun test(): Unit = Unit
    }

    @Test
    fun `함수가 정의된 클래스가 object가 아니면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Module2::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module2::test.name} 함수가 정의된 클래스가 object가 아닙니다.")
        }
    }

    @Module
    class Module2 {
        @Component
        fun test(): Unit = Unit
    }

    @Test
    fun `함수가 정의된 클래스에 @Module이 붙어 있지 않다면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Module3::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module3::test.name} 함수가 정의된 클래스에 @Module이 붙어있어야 합니다.")
        }
    }

    object Module3 {
        @Component
        fun test(): Unit = Unit
    }

    @Test
    fun `함수의 리턴 타입이 nullable이면 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Module4::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("SheathComponent를 생성할 함수의 리턴 타입이 nullable이면 안됩니다")
        }
    }

    @Module
    object Module4 {
        @Component
        fun test(): Unit? = Unit
    }

    @Test
    fun `함수의 매개 변수 중 같은 타입이 있다면 한정자가 있어도 에러가 발생한다`() {
        try {
            SheathComponentValidator.validate(Module5::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module5::test.name} 함수는 같은 타입을 여러 매개 변수로 가지고 있습니다.")
        }
    }

    @Module
    object Module5 {
        @Component
        fun test(
            @Qualifier(Test5::class) test5: Test4,
            @Qualifier(Test6::class) test6: Test4,
        ): Unit = Unit
    }
}
