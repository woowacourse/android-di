package com.woowa.di.injection

import com.woowa.di.fixture.DIFailTest
import com.woowa.di.fixture.DITest
import com.woowa.di.fixture.FakeDI
import com.woowa.di.fixture.FakeModule
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows


class InjectedViewModelFactoryKtTest {
    @Before
    fun setUp() {
        ModuleRegistry.registerModule(
            FakeDI::class, FakeModule::class
        )
    }

    @Test
    fun `자동 DI가 주입된 객체가 생성된다`() {
        assertDoesNotThrow {
            createInjectedInstance(DITest::class)
        }
    }

    @Test
    fun `DIInject 어노테이션이 없을 경우, 예외가 발생한다`() {
        assertThrows<IllegalStateException> {
            createInjectedInstance(DIFailTest::class)
        }
    }

}