package com.kmlibs.supplin

import android.content.Context
import com.kmlibs.supplin.fixtures.Module1
import com.kmlibs.supplin.fixtures.Module2
import io.mockk.mockk
import org.junit.Test
import com.google.common.truth.Truth.assertThat

class InjectorTest {
    private lateinit var instanceContainer: InstanceContainer

    @Test
    fun `Injector should be initialized by explicitly calling init`() {
        val mockContext = mockk<Context>(relaxed = true)
        Injector.init {
            context(mockContext)
            module(Module1::class, Module2::class)
        }
        instanceContainer = Injector.instanceContainer
        assertThat(::instanceContainer.isInitialized).isTrue()
    }
}
