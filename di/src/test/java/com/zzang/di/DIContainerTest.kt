package com.zzang.di

import com.google.common.truth.Truth.assertThat
import com.zzang.di.testfixture.TestClass
import org.junit.Test

class DIContainerTest {
    @Test
    fun `싱글톤 객체 테스트(객체 1번 생성)`() {
        // given
        DIContainer.registerSingletonInstance(TestClass::class, TestClass())

        // when
        val testClass1 = DIContainer.resolve(TestClass::class)
        val testClass2 = DIContainer.resolve(TestClass::class)

        // then
        assertThat(testClass1).isSameInstanceAs(testClass2)
    }
}
