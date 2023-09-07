package com.ki960213.sheath.instantiater

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import kotlin.reflect.full.createType

internal class InstantiaterKtTest {

    @Test
    fun `특정 클래스의 종속 항목 인스턴스가 존재한다면 그 클래스의 인스턴스를 반환한다`() {
        val instances = listOf(Test2())

        val result = Test1::class.instantiateByPrimaryConstructor(instances)

        assertThat(result).isInstanceOf(Test1::class.java)
    }

    @Test
    fun `특정 클래스의 주생성자에 매개변수가 없다면 그 클래스의 인스턴스를 반환한다`() {
        val result = Test2::class.instantiateByPrimaryConstructor(emptyList())

        assertThat(result).isInstanceOf(Test2::class.java)
    }

    @Test
    fun `특정 클래스의 종속 항목 인스턴스가 존재하지 않는다면 에러가 발생한다`() {
        val instances = emptyList<Any>()

        try {
            Test1::class.instantiateByPrimaryConstructor(instances)
        } catch (e: IllegalArgumentException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("인스턴스 목록에 ${Test2::class.createType()} 인스턴스가 존재하지 않습니다.")
        }
    }

    // 인스턴스 생성 해피 케이스 테스트 용
    class Test1(val test2: Test2)

    class Test2
}
