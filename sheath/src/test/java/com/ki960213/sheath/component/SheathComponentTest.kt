package com.ki960213.sheath.component

import com.google.common.truth.Expect
import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Inject
import com.ki960213.sheath.annotation.Qualifier
import org.junit.Rule
import org.junit.Test
import kotlin.reflect.full.createType

internal class SheathComponentTest {

    @JvmField
    @Rule
    val expect: Expect = Expect.create()

    @Test
    fun `SheathComponent의 타입이 같으면 같다고 판단한다`() {
        val sheathComponent1 = ClassSheathComponent(Test1::class)
        val sheathComponent2 = ClassSheathComponent(Test1::class)

        assertThat(sheathComponent1).isEqualTo(sheathComponent2)
    }

    @Component
    open class Test1

    @Test
    fun `SheathComponent의 타입이 다르면 다르다고 판단한다`() {
        val sheathComponent1 = ClassSheathComponent(Test1::class)
        val sheathComponent2 = ClassSheathComponent(Test2::class)

        assertThat(sheathComponent1).isNotEqualTo(sheathComponent2)
    }

    @Component
    class Test2 : Test1()

    @Test
    fun `SheathComponent의 해시 코드는 SheathComponent의 타입의 해시 코드와 같다`() {
        val sheathComponent = ClassSheathComponent(Test1::class)

        val actual = sheathComponent.hashCode()

        assertThat(actual).isEqualTo(Test1::class.createType().hashCode())
    }

    @Test
    fun `SheathComponent의 문자열은 타입을 담은 문자열이다`() {
        val sheathComponent = ClassSheathComponent(Test1::class)

        val actual = sheathComponent.toString()

        assertThat(actual).contains(Test1::class.createType().toString())
    }

    @Test
    fun `SheathComponent의 종속 항목 중 다른 SheathComponent의 타입의 슈퍼 타입인 종속 항목이 없다면 의존하지 않는 것이다`() {
        val sheathComponent1 = ClassSheathComponent(Test3::class)
        val sheathComponent2 = ClassSheathComponent(Test7::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isFalse()
    }

    @Component
    class Test3(test4: Test4) {
        @Inject
        private lateinit var test5: Test5

        @Inject
        private fun test(test6: Test6): Unit = Unit
    }

    @Component
    class Test4

    @Component
    class Test5

    @Component
    class Test6

    @Component
    class Test7

    @Test
    fun `SheathComponent의 종속 항목 중 다른 SheathComponent의 타입과 같은 타입의 종속 항목이 있고 한정자가 없다면 의존하는 것이다`() {
        val sheathComponent1 = ClassSheathComponent(Test3::class)
        val sheathComponent2 = ClassSheathComponent(Test4::class)
        val sheathComponent3 = ClassSheathComponent(Test5::class)
        val sheathComponent4 = ClassSheathComponent(Test6::class)

        expect.that(sheathComponent1.isDependingOn(sheathComponent2)).isTrue()
        expect.that(sheathComponent1.isDependingOn(sheathComponent3)).isTrue()
        expect.that(sheathComponent1.isDependingOn(sheathComponent4)).isTrue()
    }

    @Test
    fun `SheathComponent의 종속 항목 중 다른 SheathComponent의 타입의 슈퍼 타입의 종속 항목이 있고 한정자가 없다면 의존하는 것이다`() {
        val sheathComponent1 = ClassSheathComponent(Test8::class)
        val sheathComponent2 = ClassSheathComponent(Test10::class)
        val sheathComponent3 = ClassSheathComponent(Test12::class)

        expect.that(sheathComponent1.isDependingOn(sheathComponent2)).isTrue()
        expect.that(sheathComponent1.isDependingOn(sheathComponent3)).isTrue()
    }

    @Component
    class Test8(test10: Test10) {
        @Inject
        private lateinit var test11: Test11
    }

    open class Test9

    @Component
    class Test10 : Test9()

    interface Test11

    @Component
    class Test12 : Test11

    @Test
    fun `SheathComponent의 종속 항목 중 다른 SheathComponent의 타입의 슈퍼 타입의 종속 항목이 있어도 한정자에 설정된 클래스가 다른 SheathComponent의 타입의 클래스와 다르다면 의존하지 않는 것이다`() {
        val sheathComponent1 = ClassSheathComponent(Test13::class)
        val sheathComponent2 = ClassSheathComponent(Test16::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isFalse()
    }

    @Component
    class Test13(
        @Qualifier(Test15::class)
        test14: Test14,
    )

    interface Test14

    @Component
    class Test15 : Test14

    @Component
    class Test16 : Test14

    @Test
    fun `SheathComponent의 종속 항목 중 다른 SheathComponent의 타입의 슈퍼 타입의 종속 항목이 있을 때 한정자에 설정된 클래스가 다른 SheathComponent의 타입의 클래스와 같다면 의존하는 것이다`() {
        val sheathComponent1 = ClassSheathComponent(Test13::class)
        val sheathComponent2 = ClassSheathComponent(Test15::class)

        val actual = sheathComponent1.isDependingOn(sheathComponent2)

        assertThat(actual).isTrue()
    }
}
