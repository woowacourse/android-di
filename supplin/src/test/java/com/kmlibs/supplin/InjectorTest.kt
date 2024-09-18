package com.kmlibs.supplin

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import io.mockk.mockk
import org.junit.Test

class InjectorTest {
    private lateinit var instanceContainer: InstanceContainer

    @Test
    fun `Injector should be initialized by explicitly calling init`() {
        val mockContext = mockk<Context>(relaxed = true)
        Injector.init {
            context(mockContext)
            module(
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class
            )
        }
        instanceContainer = Injector.instanceContainer
        assertThat(::instanceContainer.isInitialized).isTrue()
    }

    @Test
    fun `InstanceContainer in Injector cannot be initialized repeatedly`() {
        // given
        val mockContext = mockk<Context>(relaxed = true)

        // when
        Injector.init {
            context(mockContext)
            module(FakeConcreteModule::class, FakeRepositoryModule::class, FakeDataSourceModule::class)
        }
        val firstInstanceContainer = Injector.instanceContainer

        Injector.init {
            context(mockContext)
            module(FakeConcreteModule::class, FakeRepositoryModule::class, FakeDataSourceModule::class)
        }
        val secondInstanceContainer = Injector.instanceContainer

        // then
        assertThat(firstInstanceContainer).isSameInstanceAs(secondInstanceContainer)
    }
}
