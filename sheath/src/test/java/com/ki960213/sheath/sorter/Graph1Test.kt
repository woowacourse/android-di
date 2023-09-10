package com.ki960213.sheath.sorter

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.component.SheathComponent
import org.junit.Test

internal class Graph1Test {

    @Test
    fun `그래프를 만들 때 어떤 노드가 노드 목록에 없는 노드를 의존한다면 에러가 발생한다`() {
        val nodes = setOf(
            Node1(SheathComponent(Test1::class)),
        )

        try {
            Graph1(nodes)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${SheathComponent(Test1::class)} 컴포넌트의 종속 항목 중 등록되지 않은 컴포넌트가 있습니다.")
        }
    }

    private class Test1(val test2: Test2)

    private class Test2

    @Test
    fun `그래프를 만들 때 어떤 노드의 중복 종속 항목이 존재한다면 에러가 발생한다`() {
        val nodes = setOf(
            Node1(SheathComponent(Test3::class)),
            Node1(SheathComponent(Test5::class)),
            Node1(SheathComponent(Test6::class)),
        )

        try {
            Graph1(nodes)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${SheathComponent(Test3::class)} 컴포넌트에 모호한 종속 항목이 존재합니다.")
        }
    }

    private class Test3(test5: Test5, test6: Test6)

    private open class Test4

    private class Test5 : Test4()

    private class Test6 : Test4()
}
