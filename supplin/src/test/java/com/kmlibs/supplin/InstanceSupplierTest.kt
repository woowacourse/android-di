package com.kmlibs.supplin

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.FieldInjectionObj
import com.kmlibs.supplin.fixtures.Foo2
import com.kmlibs.supplin.fixtures.Module1
import com.kmlibs.supplin.fixtures.Module2
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class InstanceSupplierTest {
    @Before
    fun setUp() {
        Injector.init {
            val mockContext = mockk<Context>(relaxed = true)
            context(mockContext)
            module(Module1::class, Module2::class)
        }
    }

    @Test
    fun `injectFields should inject fields with @Supply annotation`() {
        val targetInstance = FieldInjectionObj()
        val clazz = targetInstance::class
        InstanceSupplier.injectFields(clazz, targetInstance)
        assertThat(targetInstance.foo).isInstanceOf(Foo2::class.java)
    }
}
