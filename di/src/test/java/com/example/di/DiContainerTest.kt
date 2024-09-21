package com.example.di

import com.google.common.truth.Truth
import org.junit.Test

class DiContainerTest {
    @Test
    fun `DiContainer의 getInstanceOrNull메서드는 모듈에서 인스턴스를 찾아 반환한다`() {
        val container = DiContainer()
        container.addModule(DatabaseModule)
        val result = container.getInstanceOrNull(TestDao::class)
        Truth.assertThat(result).isInstanceOf(TestDao::class.java)
    }
}
