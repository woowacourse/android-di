package com.ki960213.sheath.component

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Module
import com.ki960213.sheath.annotation.NewInstance
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.full.createType

internal class DependenciesExtractorTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `클래스의 생성자에 @Inject가 붙어 있다면 그 생성자의 매개 변수들의 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test1::class)

        expect.that(actual[Test2::class.createType()])
            .isEqualTo(DependentCondition(false, Test2::class))
        expect.that(actual[Test3::class.createType()])
            .isEqualTo(DependentCondition(true, null))
    }

    @Component
    class Test1 @Inject constructor(
        @Qualifier(Test2::class)
        test2: Test2,
        @NewInstance
        test3: Test3,
    )

    @Component
    class Test2

    @Component
    class Test3

    @Test
    fun `클래스의 프로퍼티에 @Inject가 붙어 있다면 그 프로퍼티의 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test4::class)

        expect.that(actual[Test5::class.createType()])
            .isEqualTo(DependentCondition(false, Test5::class))
        expect.that(actual[Test6::class.createType()])
            .isEqualTo(DependentCondition(true, null))
    }

    @Component
    class Test4 {
        @Qualifier(Test5::class)
        @Inject
        private lateinit var test5: Test5

        @NewInstance
        @Inject
        private lateinit var test6: Test6
    }

    @Component
    class Test5

    @Component
    class Test6

    @Test
    fun `클래스의 함수에 @Inject가 붙어 있다면 그 함수의 매개 변수들의 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test7::class)

        expect.that(actual[Test8::class.createType()])
            .isEqualTo(DependentCondition(true, null))
        expect.that(actual[Test9::class.createType()])
            .isEqualTo(DependentCondition(false, Test9::class))
    }

    @Component
    class Test7 {

        @Inject
        private fun test1(@NewInstance test8: Test8): Unit = Unit

        @Inject
        fun test2(@Qualifier(Test9::class) test9: Test9): Unit = Unit
    }

    @Component
    class Test8

    @Component
    class Test9

    @Test
    fun `클래스의 생성자, 프로퍼티, 함수에 @Inject가 붙어 있다면 모두 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test10::class)

        expect.that(actual[Test11::class.createType()])
            .isEqualTo(DependentCondition(false, null))
        expect.that(actual[Test12::class.createType()])
            .isEqualTo(DependentCondition(false, Test12::class))
        expect.that(actual[Test13::class.createType()])
            .isEqualTo(DependentCondition(true, null))
    }

    @Component
    class Test10(any: Any) {
        @Inject
        constructor(test11: Test11) : this(test11 as Any)

        @Qualifier(Test12::class)
        @Inject
        private lateinit var test12: Test12

        @Inject
        private fun test(@NewInstance test13: Test13): Unit = Unit
    }

    @Component
    class Test11

    @Component
    class Test12

    @Component
    class Test13

    @Test
    fun `클래스에 @Inject가 붙은 요소가 없다면 주 생성자의 매개 변수의 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test14::class)

        expect.that(actual[Test15::class.createType()])
            .isEqualTo(DependentCondition(false, null))
    }

    @Component
    class Test14(test15: Test15) {
        constructor(test16: Test16) : this(test16 as Test15)

        private lateinit var test17: Test17

        private fun test(test18: Test18): Unit = Unit
    }

    @Component
    open class Test15

    @Component
    class Test16 : Test15()

    @Component
    class Test17

    @Component
    class Test18

    @Test
    fun `클래스에 @Inject가 붙은 요소가 없고 주 생성자의 매개 변수가 없다면 빈 목록을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Test19::class)

        assertThat(actual).isEmpty()
    }

    @Component
    class Test19

    @Test
    fun `함수의 매개 변수들의 의존 조건을 추출한다`() {
        val actual = DependenciesExtractor.extractFrom(Module1::test)

        expect.that(actual[Test20::class.createType()])
            .isEqualTo(DependentCondition(false, Test20::class))
        expect.that(actual[Test21::class.createType()])
            .isEqualTo(DependentCondition(true, null))
    }

    @Module
    object Module1 {
        @Component
        fun test(
            @Qualifier(Test20::class)
            test20: Test20,
            @NewInstance
            test21: Test21,
        ): Unit = Unit
    }

    @Component
    class Test20

    @Component
    class Test21
}
