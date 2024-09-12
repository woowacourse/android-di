package com.kmlibs.supplin

import android.content.Context
import com.kmlibs.supplin.fixtures.Foo
import com.kmlibs.supplin.fixtures.FooQualifier1
import com.kmlibs.supplin.fixtures.Module1
import com.kmlibs.supplin.fixtures.Module2
import io.mockk.mockk
import kotlin.reflect.full.primaryConstructor
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.Foos
import org.junit.Before
import org.junit.Test

class InstanceContainerTest {
    private lateinit var instanceContainer: InstanceContainer

    @Before
    fun setUp() {
        val context = mockk<Context>(relaxed = true)
        val modules = listOf(Module1, Module2)
        instanceContainer = InstanceContainer(context, modules)
    }

    @Test
    fun `KParameter instanceOf should return the correct instance`() {
        val primaryConstructor = Foos::class.primaryConstructor ?: error("no primary constructor")

        val primaryConstructorParameters = primaryConstructor.parameters
        primaryConstructorParameters.forEach { parameter ->
            val instance = instanceContainer.instanceOf<Any>(parameter)
            assertThat(instance).isInstanceOf(Foo::class.java)
        }
    }
}
