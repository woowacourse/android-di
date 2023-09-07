package com.ki960213.sheath.sorter

import com.google.common.truth.Truth
import org.junit.Test

internal class SorterKtTest {
    @Test
    fun `클래스들을 받으면 위상 정렬된 클래스들을 반환한다`() {
        val classes =
            listOf(Test1::class.java, Test2::class.java, Test3::class.java, Test4::class.java)

        val result = classes.sortedTopologically()

        val expected = listOf(
            Test4::class.java,
            Test2::class.java,
            Test3::class.java,
            Test1::class.java,
        )
        Truth.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun `클래스들 간에 의존 사이클이 존재하면 에러가 발생한다`() {
        val classes = listOf(
            Test5::class.java,
            Test6::class.java,
            Test7::class.java,
        )

        try {
            classes.sortedTopologically()
        } catch (e: IllegalStateException) {
            Truth.assertThat(e).hasMessageThat()
                .isEqualTo("클래스 간 의존 사이클이 존재합니다.")
        }
    }

    // 위상 정렬 기능 해피 케이스 테스트 용
    private class Test1(val test2: Test2, val test3: Test3)

    private class Test2(val test4: Test4)

    private class Test3(val test4: Test4)

    private class Test4

    // 의존 사아클 테스트 용
    private class Test5(val test6: Test6)

    private class Test6(val test7: Test7)

    private class Test7(val test5: Test5)
}
