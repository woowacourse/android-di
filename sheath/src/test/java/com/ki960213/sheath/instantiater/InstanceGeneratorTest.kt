package com.ki960213.sheath.instantiater

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class InstanceGeneratorTest {

    @Test
    fun `특정 클래스의 종속 항목 인스턴스가 존재한다면 그 클래스의 인스턴스를 반환한다`() {
        val instances = listOf(Test2())

        val result = InstanceGenerator.generate(Test1::class.java, instances)

        assertThat(result).isInstanceOf(Test1::class.java)
    }

    @Test
    fun `특정 클래스의 주생성자가 존재하지 않는다면 그 클래스의 인스턴스를 반환한다`() {
        val result = InstanceGenerator.generate(Test2::class.java, emptyList())

        assertThat(result).isInstanceOf(Test2::class.java)
    }

    @Test
    fun `특정 클래스의 주생성자에 매개변수가 없다면 그 클래스의 인스턴스를 반환한다`() {
        val result = InstanceGenerator.generate(Test3::class.java, emptyList())

        assertThat(result).isInstanceOf(Test3::class.java)
    }

    @Test
    fun `특정 클래스의 종속 항목 인스턴스가 존재하지 않는다면 에러가 발생한다`() {
        val instances = emptyList<Class<*>>()

        try {
            InstanceGenerator.generate(Test1::class.java, instances)
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test1::class.java.name}의 종속 항목인 ${Test2::class.java.name}의 인스턴스가 존재하지 않습니다.")
        }
    }

    @Test
    fun `특정 클래스의 특정 매개변수에 들어갈 수 있는 종속 항목 인스턴스가 여러 개라면 에러가 발생한다`() {
        val instances = listOf(Test5(), Test6())

        try {
            InstanceGenerator.generate(Test7::class.java, instances)
        } catch (e: IllegalStateException) {
            assertThat(e).hasMessageThat()
                .isEqualTo("${Test7::class.java.name}의 종속 항목인 ${Test4::class.java.name}의 인스턴스가 여러 개 존재합니다.")
        }
    }

    // 인스턴스 생성 해피 케이스 테스트 용
    class Test1(val test2: Test2)

    class Test2

    // 매개변수가 없는 주 생성자 인스턴스 생성 테스트 용
    class Test3 constructor()

    // 중복 종속 항목 인스턴스 테스트 용
    interface Test4

    class Test5 : Test4

    class Test6 : Test4

    class Test7(val test4: Test4)
}
