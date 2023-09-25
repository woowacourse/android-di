package com.ki960213.sheath.component

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Test
import kotlin.reflect.full.createType

internal class FunctionSheathComponentTest {

    @Test
    fun `함수에 Component 애노테이션이 붙어있지 않고 Component 애노테이션이 붙은 애노테이션이 붙어 있지 않다면 에러가 발생한다`() {
        try {
            FunctionSheathComponent(Module1001::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("함수에 @Component 혹은 @Component가 붙은 애노테이션이 붙어 있지 않다면 SheathComponent를 생성할 수 없습니다.")
        }
    }

    @Module
    object Module1001 {
        fun test(): Unit = Unit
    }

    @Test
    fun `함수가 정의된 클래스가 object가 아니면 에러가 발생한다`() {
        try {
            FunctionSheathComponent(Module1002::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module1002::test.name} 함수가 정의된 클래스가 object가 아닙니다.")
        }
    }

    @Module
    class Module1002 {
        @Component
        fun test(): Unit = Unit
    }

    @Test
    fun `함수가 정의된 클래스에 @Module이 붙어 있지 않다면 에러가 발생한다`() {
        try {
            FunctionSheathComponent(Module1003::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module1003::test.name} 함수가 정의된 클래스에 @Module이 붙어있어야 합니다.")
        }
    }

    object Module1003 {
        @Component
        fun test(): Unit = Unit
    }

    @Test
    fun `함수의 리턴 타입이 nullable이면 에러가 발생한다`() {
        try {
            FunctionSheathComponent(Module1004::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("SheathComponent를 생성할 함수의 리턴 타입이 nullable이면 안됩니다")
        }
    }

    @Module
    object Module1004 {
        @Component
        fun test(): Unit? = Unit
    }

    @Test
    fun `함수의 매개 변수 중 같은 타입이 있다면 한정자가 있어도 에러가 발생한다`() {
        try {
            FunctionSheathComponent(Module1005::test)
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module1005::test.name} 함수는 같은 타입을 여러 매개 변수로 가지고 있습니다.")
        }
    }

    @Module
    object Module1005 {
        @Component
        fun test(
            @Qualifier(Test1005::class) test1005: Test1004,
            @Qualifier(Test1006::class) test1006: Test1004,
        ): Unit = Unit
    }

    interface Test1004

    @Component
    class Test1005 : Test1004

    @Component
    class Test1006 : Test1004

    fun `FunctionSheathComponent 객체를 생성하면 타입은 함수의 리턴 타입과 같다`() {
        val actual = FunctionSheathComponent(Module101::test)

        assertThat(actual.type).isEqualTo(Test105::class.createType())
    }

    @Module
    object Module101 {
        @Component
        fun test(): Test105 = Test105()
    }

    class Test105

    fun `FunctionSheathComponent 객체를 생성하면 이름은 함수의 리턴 타입 클래스의 qualifiedName과 같다`() {
        val actual = FunctionSheathComponent(Module101::test)

        assertThat(actual.name).isEqualTo(Test105::class.qualifiedName)
    }

    fun `FunctionSheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = FunctionSheathComponent(Module102::test)

        assertThat(actual.isSingleton).isTrue()
    }

    @Module
    object Module102 {
        @Prototype
        @Component
        fun test(): Unit = Unit
    }

    fun `FunctionSheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙은 애노테이션이 붙어 있다면 싱글톤이다`() {
        val actual = FunctionSheathComponent(Module103::test)

        assertThat(actual.isSingleton).isTrue()
    }

    @Module
    object Module103 {
        @PrototypeAttached
        @Component
        fun test(): Unit = Unit
    }

    @Prototype
    annotation class PrototypeAttached

    fun `FunctionSheathComponent 객체를 생성할 때 함수에 Prototype 애노테이션이 붙은 애노테이션과 Prototype 애노테이션이 붙어 있지 않다면 싱글톤이 아니다`() {
        val actual = FunctionSheathComponent(Module104::test)

        assertThat(actual.isSingleton).isFalse()
    }

    @Module
    object Module104 {
        @Component
        fun test(): Unit = Unit
    }

    // 왜 테스트에서는 Module 클래스의 인스턴스를 함수의 인자로 넣으면 안되고, 실제 코드에서는 Module 클래스의 인스턴스를 함수의 인자로 넣어줘야 하는 지 모르겠습니다.
    // 누가 알려주세요ㅠㅠ
//    @JvmField
//    @Rule
//    val expect: Expect = Expect.create()
//
//    @Test
//    fun `인스턴스화 할 때 함수의 매개 변수에 주입할 수 있는 SheathComponent가 부족하면 에러가 발생한다`() {
//        val sheathComponent = FunctionSheathComponent(Module1::test)
//
//        try {
//            sheathComponent.instantiate(emptyList())
//        } catch (e: IllegalArgumentException) {
//            assertThat(e)
//                .hasMessageThat()
//                .isEqualTo("${Module1::test.name} 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
//        }
//    }
//
//    @Module
//    object Module1 {
//        @Component
//        fun test(test1: Test1): Unit = Unit
//    }
//
//    class Test1
//
//    @Test
//    fun `인스턴스화 할 때 한정자가 설정되어 있는 종속 항목이 주어지지 않았다면 에러가 발생한다`() {
//        val sheathComponent1 = FunctionSheathComponent(Module7::test1)
//        val sheathComponent2 = FunctionSheathComponent(Module7::test2)
//        sheathComponent1.instantiate(emptyList())
//        try {
//            sheathComponent2.instantiate(listOf(sheathComponent1))
//        } catch (e: IllegalArgumentException) {
//            assertThat(e)
//                .hasMessageThat()
//                .isEqualTo("${Module7::test2.name} 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
//        }
//    }
//
//    @Module
//    object Module7 {
//        @Component
//        fun test1(): Test14 = Test14()
//
//        @Component
//        fun test2(@Qualifier(Test15::class) test13: Test13): Unit = Unit
//    }
//
//    interface Test13
//
//    class Test14 : Test13
//
//    class Test15 : Test13
//
//    @Test
//    fun `인스턴스화 하면 인스턴스가 할당된다`() {
//        val sheathComponent1 = FunctionSheathComponent(Module2::test1)
//        val sheathComponent2 = FunctionSheathComponent(Module2::test2)
//        val sheathComponent3 = FunctionSheathComponent(Module2::test3)
//        sheathComponent1.instantiate(emptyList())
//        sheathComponent2.instantiate(emptyList())
//        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))
//
//        val actual = sheathComponent3.instance
//
//        assertThat(actual).isInstanceOf(Test3::class.java)
//    }
//
//    @Module
//    object Module2 {
//        @Component
//        fun test1(): Test1 = Test1()
//
//        @Component
//        fun test2(): Test2 = Test2()
//
//        @Component
//        fun test3(test1: Test1, test2: Test2): Test3 = Test3(test1, test2)
//    }
//
//    class Test2
//
//    class Test3(test1: Test1, test2: Test2)
//
//    @Test
//    fun `새로운 인스턴스를 반환하면 이전 인스턴스와 다른 새 인스턴스가 반환된다`() {
//        val sheathComponent = FunctionSheathComponent(Module3::test)
//        sheathComponent.instantiate(emptyList())
//
//        val actual = sheathComponent.getNewInstance()
//
//        assertThat(actual).isNotEqualTo(sheathComponent.instance)
//    }
//
//    @Module
//    object Module3 {
//        @Component
//        fun test(): Test1 = Test1()
//    }
//
//    @Test
//    fun `새 인스턴스 반환 시 함수의 종속 항목이 싱글톤이면 같은 종속 항목을 주입받는다`() {
//        val sheathComponent1 = FunctionSheathComponent(Module4::test1)
//        val sheathComponent2 = FunctionSheathComponent(Module4::test2)
//        val sheathComponent3 = FunctionSheathComponent(Module4::test3)
//        sheathComponent1.instantiate(emptyList())
//        sheathComponent2.instantiate(emptyList())
//        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))
//
//        val actual = sheathComponent3.getNewInstance() as Test6
//
//        expect.that(actual.test4).isEqualTo(sheathComponent1.instance)
//        expect.that(actual.test5).isEqualTo(sheathComponent2.instance)
//    }
//
//    @Module
//    object Module4 {
//        @Component
//        fun test1(): Test4 = Test4()
//
//        @Component
//        fun test2(): Test5 = Test5()
//
//        @Component
//        fun test3(test4: Test4, test5: Test5): Test6 = Test6(test4, test5)
//    }
//
//    class Test4
//
//    class Test5
//
//    class Test6(val test4: Test4, val test5: Test5)
//
//    @Test
//    fun `새 인스턴스 반환 시 함수의 종속 항목이 싱글톤이 아니면 새 종속 항목을 주입받는다`() {
//        val sheathComponent1 = FunctionSheathComponent(Module5::test1)
//        val sheathComponent2 = FunctionSheathComponent(Module5::test2)
//        val sheathComponent3 = FunctionSheathComponent(Module5::test3)
//        sheathComponent1.instantiate(emptyList())
//        sheathComponent2.instantiate(emptyList())
//        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))
//
//        val actual = sheathComponent3.getNewInstance() as Test9
//
//        expect.that(actual.test7).isNotEqualTo(sheathComponent1.instance)
//        expect.that(actual.test8).isNotEqualTo(sheathComponent2.instance)
//    }
//
//    @Module
//    object Module5 {
//        @Prototype
//        @Component
//        fun test1(): Test7 = Test7()
//
//        @Prototype
//        @Component
//        fun test2(): Test8 = Test8()
//
//        @Component
//        fun test3(test7: Test7, test8: Test8): Test9 = Test9(test7, test8)
//    }
//
//    class Test7
//
//    class Test8
//
//    class Test9(val test7: Test7, val test8: Test8)
//
//    @Test
//    fun `새 인스턴스 반환 시 함수의 매개 변수에 @NewInstance가 붙어있다면 새 종속 항목을 주입받는다`() {
//        val sheathComponent1 = FunctionSheathComponent(Module6::test1)
//        val sheathComponent2 = FunctionSheathComponent(Module6::test2)
//        val sheathComponent3 = FunctionSheathComponent(Module6::test3)
//        sheathComponent1.instantiate(emptyList())
//        sheathComponent2.instantiate(emptyList())
//        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))
//
//        val actual = sheathComponent3.getNewInstance() as Test12
//
//        expect.that(actual.test10).isNotEqualTo(sheathComponent1.instance)
//        expect.that(actual.test11).isNotEqualTo(sheathComponent2.instance)
//    }
//
//    @Module
//    object Module6 {
//        @Component
//        fun test1(): Test10 = Test10()
//
//        @Component
//        fun test2(): Test11 = Test11()
//
//        @Component
//        fun test3(
//            @NewInstance test10: Test10,
//            @NewInstance test11: Test11,
//        ): Test12 = Test12(test10, test11)
//    }
//
//    class Test10
//
//    class Test11
//
//    class Test12(val test10: Test10, val test11: Test11)
}
