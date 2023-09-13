package com.ki960213.sheath.sorter

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.component.SheathComponentByClass
import org.junit.Test

internal class Sorter1KtTest {

    @Test
    fun `SheathComponent 목록을 받으면 위상 정렬된 목록을 반환한다`() {
        val components =
            listOf(
                SheathComponentByClass(Test1::class),
                SheathComponentByClass(Test2::class),
                SheathComponentByClass(Test3::class),
                SheathComponentByClass(Test4::class),
            )

        val result = components.sorted()

        val expected = listOf(
            SheathComponentByClass(Test4::class),
            SheathComponentByClass(Test2::class),
            SheathComponentByClass(Test3::class),
            SheathComponentByClass(Test1::class),
        )
        assertThat(result).isEqualTo(expected)
    }

    private class Test1(val test2: Test2, val test3: Test3)

    private class Test2(val test4: Test4)

    private class Test3(val test4: Test4)

    private class Test4

    @Test
    fun `SheathComponent 간에 의존 사이클이 존재하면 에러가 발생한다`() {
        val components = listOf(
            SheathComponentByClass(Test5::class),
            SheathComponentByClass(Test6::class),
            SheathComponentByClass(Test7::class),
        )

        try {
            components.sorted()
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("SheathComponent 간 의존 사이클이 존재합니다.")
        }
    }

    private class Test5(val test6: Test6)

    private class Test6(val test7: Test7)

    private class Test7(val test5: Test5)
}
