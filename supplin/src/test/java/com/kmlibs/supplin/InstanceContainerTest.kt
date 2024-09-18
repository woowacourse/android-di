package com.kmlibs.supplin

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.android.datasource.FakeDataSource
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import com.kmlibs.supplin.fixtures.android.repository.DefaultFakeRepository6
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import kotlin.reflect.full.primaryConstructor

class InstanceContainerTest {
    private lateinit var instanceContainer: InstanceContainer

    @Before
    fun setUp() {
        val context = mockk<Context>(relaxed = true)
        val modules = listOf(
            FakeConcreteModule::class,
            FakeRepositoryModule::class,
            FakeDataSourceModule::class
        )
        instanceContainer = InstanceContainer(context, modules)
    }

    @Test
    fun `KParameter instanceOf should return the correct instance`() {
        val primaryConstructor =
            DefaultFakeRepository6::class.primaryConstructor ?: error("no primary constructor")

        val primaryConstructorParameters = primaryConstructor.parameters
        primaryConstructorParameters.forEach { parameter ->
            val instance = instanceContainer.instanceOf<Any>(parameter)
            assertThat(instance).isInstanceOf(FakeDataSource::class.java)
        }
    }
}
