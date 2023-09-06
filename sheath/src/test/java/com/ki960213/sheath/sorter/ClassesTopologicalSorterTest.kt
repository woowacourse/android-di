package com.ki960213.sheath.sorter

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class ClassesTopologicalSorterTest {

    @Test
    fun `클래스들을 받으면 위상 정렬된 클래스들을 반환한다`() {
        val classes =
            listOf(Test1::class.java, Test2::class.java, Test3::class.java, Test4::class.java)

        val result = ClassesTopologicalSorter.sort(classes)

        val expected = listOf(
            Test4::class.java,
            Test2::class.java,
            Test3::class.java,
            Test1::class.java,
        )
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `클래스들 중 종속 항목인 클래스가 클래스 목록에 없다면 에러가 발생한다`() {
        val classes = listOf(
            Test1::class.java,
            Test2::class.java,
            Test3::class.java,
            Test4::class.java,
            Test5::class.java,
        )

        try {
            ClassesTopologicalSorter.sort(classes)
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat().isEqualTo("${Test6::class.java.name} 클래스가 존재하지 않습니다.")
        }
    }

    @Test
    fun `클래스들 중 종속 항목인 클래스가 클래스 목록에 2개 이상 있다면 에러가 발생한다`() {
        val classes = listOf(
            Test8::class.java,
            Test9::class.java,
            Test10::class.java,
        )

        try {
            ClassesTopologicalSorter.sort(classes)
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test7::class.java.name} 클래스에 주입될 클래스가 모호합니다.")
        }
    }

    @Test
    fun `클래스들 간에 의존 사이클이 존재하면 에러가 발생한다`() {
        val classes = listOf(
            Test11::class.java,
            Test12::class.java,
            Test13::class.java,
        )

        try {
            ClassesTopologicalSorter.sort(classes)
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("클래스들 간에 의존 사이클이 존재합니다.")
        }
    }

    // 위상 정렬 기능 해피 케이스 테스트 용
    class Test1(val test2: Test2, val test3: Test3)

    class Test2(val test4: Test4)

    class Test3(val test4: Test4)

    class Test4

    // 종속 항목 부재 테스트 용
    class Test5(val test6: Test6)

    class Test6

    // 모호한 의존성 테스트 용
    open class Test7

    class Test8 : Test7()

    class Test9 : Test7()

    class Test10(val test7: Test7)

    // 의존 사아클 테스트 용
    class Test11(val test12: Test12)

    class Test12(val test13: Test13)

    class Test13(val test11: Test11)
}
