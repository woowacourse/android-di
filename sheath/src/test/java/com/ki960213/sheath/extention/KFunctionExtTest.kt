package com.ki960213.sheath.extention

import com.google.common.truth.Truth.assertThat
import com.ki960213.sheath.annotation.Component
import com.ki960213.sheath.annotation.Module
import org.junit.Test
import kotlin.reflect.full.createType

internal class KFunctionExtTest {
    @Test
    fun `함수의 매개 변수의 타입 목록을 반환한다`() {
        val actual = Module1::test.getDependingTypes()

        assertThat(actual).containsExactly(Test1::class.createType(), Test2::class.createType())
    }

    @Module
    object Module1 {
        @Component
        fun test(test1: Test1, test2: Test2): Unit = Unit
    }

    class Test1

    class Test2
}
