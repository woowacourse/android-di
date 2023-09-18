package com.ki960213.sheath.extention

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import org.junit.Test
import kotlin.reflect.full.createType

internal class KClassExtTest {

    @Test
    fun `클래스의 @Inject가 붙은 생성자의 매개 변수의 타입 목록을 반환한다`() {
        val actual = Test1::class.getDependingTypes()

        assertThat(actual).containsExactly(Test2::class.createType(), Test3::class.createType())
    }

    @Component
    class Test1 @Inject constructor(test2: Test2, test3: Test3)

    @Component
    class Test2

    @Component
    class Test3

    @Test
    fun `클래스의 @Inject가 붙은 생성자가 없다면 주 생성자의 매개 변수의 타입 목록을 반환한다`() {
        val actual = Test4::class.getDependingTypes()

        assertThat(actual).containsExactly(Test5::class.createType(), Test6::class.createType())
    }

    @Component
    class Test4 @Inject constructor(test5: Test5, test6: Test6)

    @Component
    class Test5

    @Component
    class Test6

    @Test
    fun `클래스의 @Inject가 붙은 프로퍼티의 타입 목록을 반환한다`() {
        val actual = Test7::class.getDependingTypes()

        assertThat(actual).containsExactly(Test8::class.createType(), Test9::class.createType())
    }

    @Component
    class Test7 {
        @Inject
        private lateinit var test8: Test8

        @Inject
        private lateinit var test9: Test9
    }

    @Component
    class Test8

    @Component
    class Test9

    @Test
    fun `클래스의 @Inject가 붙은 함수의 매개 변수의 타입 목록을 반환한다`() {
        val actual = Test10::class.getDependingTypes()

        assertThat(actual).containsExactly(
            Test11::class.createType(),
            Test12::class.createType(),
            Test13::class.createType(),
            Test14::class.createType(),
        )
    }

    @Component
    class Test10 {
        @Inject
        private fun test1(test11: Test11, test12: Test12): Unit = Unit

        @Inject
        private fun test2(test13: Test13, test14: Test14): Unit = Unit
    }

    @Component
    class Test11

    @Component
    class Test12

    @Component
    class Test13

    @Component
    class Test14

    @Test
    fun `클래스가 의존하는 타입 목록을 모두 반환한다`() {
        val actual = Test15::class.getDependingTypes()

        assertThat(actual).containsExactly(
            Test16::class.createType(),
            Test17::class.createType(),
            Test18::class.createType(),
        )
    }

    @Component
    class Test15(test16: Test16) {
        @Inject
        private lateinit var test17: Test17

        @Inject
        private fun test(test18: Test18): Unit = Unit
    }

    @Component
    class Test16

    @Component
    class Test17

    @Component
    class Test18
}
