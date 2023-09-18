package com.ki960213.sheath.component

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.NewInstance
import com.ki960213.sheath.annotation.Prototype
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Rule
import org.junit.Test

internal class FunctionSheathComponentTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `인스턴스화 할 때 함수의 매개 변수에 주입할 수 있는 SheathComponent가 부족하면 에러가 발생한다`() {
        val sheathComponent = SheathComponentFactory.create(Module1::test)

        try {
            sheathComponent.instantiate(emptyList())
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module1::test.name} 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
        }
    }

    @Module
    object Module1 {
        @Component
        fun test(test1: Test1): Unit = Unit
    }

    class Test1

    @Test
    fun `인스턴스화 할 때 한정자가 설정되어 있는 종속 항목이 주어지지 않았다면 에러가 발생한다`() {
        val sheathComponent1 = SheathComponentFactory.create(Module7::test1)
        val sheathComponent2 = SheathComponentFactory.create(Module7::test2)
        sheathComponent1.instantiate(emptyList())
        try {
            sheathComponent2.instantiate(listOf(sheathComponent1))
        } catch (e: IllegalArgumentException) {
            assertThat(e)
                .hasMessageThat()
                .isEqualTo("${Module7::test2.name} 함수의 종속 항목이 존재하지 않아 인스턴스화 할 수 없습니다.")
        }
    }

    @Module
    object Module7 {
        @Component
        fun test1(): Test14 = Test14()

        @Component
        fun test2(@Qualifier(Test15::class) test13: Test13): Unit = Unit
    }

    interface Test13

    class Test14 : Test13

    class Test15 : Test13

    @Test
    fun `인스턴스화 하면 인스턴스가 할당된다`() {
        val sheathComponent1 = SheathComponentFactory.create(Module2::test1)
        val sheathComponent2 = SheathComponentFactory.create(Module2::test2)
        val sheathComponent3 = SheathComponentFactory.create(Module2::test3)
        sheathComponent1.instantiate(emptyList())
        sheathComponent2.instantiate(emptyList())
        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))

        val actual = sheathComponent3.instance

        assertThat(actual).isInstanceOf(Test3::class.java)
    }

    @Module
    object Module2 {
        @Component
        fun test1(): Test1 = Test1()

        @Component
        fun test2(): Test2 = Test2()

        @Component
        fun test3(test1: Test1, test2: Test2): Test3 = Test3(test1, test2)
    }

    class Test2

    class Test3(test1: Test1, test2: Test2)

    @Test
    fun `새로운 인스턴스를 반환하면 이전 인스턴스와 다른 새 인스턴스가 반환된다`() {
        val sheathComponent = SheathComponentFactory.create(Module3::test)
        sheathComponent.instantiate(emptyList())

        val actual = sheathComponent.getNewInstance()

        assertThat(actual).isNotEqualTo(sheathComponent.instance)
    }

    @Module
    object Module3 {
        @Component
        fun test(): Test1 = Test1()
    }

    @Test
    fun `새 인스턴스 반환 시 함수의 종속 항목이 싱글톤이면 같은 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Module4::test1)
        val sheathComponent2 = SheathComponentFactory.create(Module4::test2)
        val sheathComponent3 = SheathComponentFactory.create(Module4::test3)
        sheathComponent1.instantiate(emptyList())
        sheathComponent2.instantiate(emptyList())
        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))

        val actual = sheathComponent3.getNewInstance() as Test6

        expect.that(actual.test4).isEqualTo(sheathComponent1.instance)
        expect.that(actual.test5).isEqualTo(sheathComponent2.instance)
    }

    @Module
    object Module4 {
        @Component
        fun test1(): Test4 = Test4()

        @Component
        fun test2(): Test5 = Test5()

        @Component
        fun test3(test4: Test4, test5: Test5): Test6 = Test6(test4, test5)
    }

    class Test4

    class Test5

    class Test6(val test4: Test4, val test5: Test5)

    @Test
    fun `새 인스턴스 반환 시 함수의 종속 항목이 싱글톤이 아니면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Module5::test1)
        val sheathComponent2 = SheathComponentFactory.create(Module5::test2)
        val sheathComponent3 = SheathComponentFactory.create(Module5::test3)
        sheathComponent1.instantiate(emptyList())
        sheathComponent2.instantiate(emptyList())
        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))

        val actual = sheathComponent3.getNewInstance() as Test9

        expect.that(actual.test7).isNotEqualTo(sheathComponent1.instance)
        expect.that(actual.test8).isNotEqualTo(sheathComponent2.instance)
    }

    @Module
    object Module5 {
        @Prototype
        @Component
        fun test1(): Test7 = Test7()

        @Prototype
        @Component
        fun test2(): Test8 = Test8()

        @Component
        fun test3(test7: Test7, test8: Test8): Test9 = Test9(test7, test8)
    }

    class Test7

    class Test8

    class Test9(val test7: Test7, val test8: Test8)

    @Test
    fun `새 인스턴스 반환 시 함수의 매개 변수에 @NewInstance가 붙어있다면 새 종속 항목을 주입받는다`() {
        val sheathComponent1 = SheathComponentFactory.create(Module6::test1)
        val sheathComponent2 = SheathComponentFactory.create(Module6::test2)
        val sheathComponent3 = SheathComponentFactory.create(Module6::test3)
        sheathComponent1.instantiate(emptyList())
        sheathComponent2.instantiate(emptyList())
        sheathComponent3.instantiate(listOf(sheathComponent1, sheathComponent2))

        val actual = sheathComponent3.getNewInstance() as Test12

        expect.that(actual.test10).isNotEqualTo(sheathComponent1.instance)
        expect.that(actual.test11).isNotEqualTo(sheathComponent2.instance)
    }

    @Module
    object Module6 {
        @Component
        fun test1(): Test10 = Test10()

        @Component
        fun test2(): Test11 = Test11()

        @Component
        fun test3(
            @NewInstance test10: Test10,
            @NewInstance test11: Test11,
        ): Test12 = Test12(test10, test11)
    }

    class Test10

    class Test11

    class Test12(val test10: Test10, val test11: Test11)
}
