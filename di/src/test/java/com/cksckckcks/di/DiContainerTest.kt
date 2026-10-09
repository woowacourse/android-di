package com.cksckckcks.di

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertSame

class DiContainerTest {
    @Test
    fun `등록한 의존성은 최초 조회에서 생성하고 재사용한다`() {
        val container = DiContainer()
        var createdCount = 0
        container.register(Dependency::class) {
            createdCount++
            Dependency()
        }

        assertEquals(0, createdCount)
        val instance = container.getInstance(Dependency::class)

        assertSame(instance, container.getInstance(Dependency::class))
        assertEquals(1, createdCount)
    }

    @Test
    fun `직접 저장한 의존성을 조회한다`() {
        val container = DiContainer()
        val instance = Dependency()

        container.saveInstance(Dependency::class, instance)

        assertSame(instance, container.getInstance(Dependency::class))
    }

    @Test
    fun `등록하지 않은 의존성은 null을 반환한다`() {
        assertNull(DiContainer().getInstance(Dependency::class))
    }

    class Dependency
}
