package com.kmlibs.supplin

import android.content.Context
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.annotations.Module
import com.kmlibs.supplin.fixtures.Module1
import com.kmlibs.supplin.fixtures.Module2
import io.mockk.mockk

class InjectionBuilderTest {
    private lateinit var injectionBuilder: InjectionBuilder

    @Before
    fun setUp() {
        injectionBuilder = InjectionBuilder()
    }

    @Test
    fun `context and modules should be set correctly`() {
        val context = mockk<Context>(relaxed = true)
        injectionBuilder.context(context)
        injectionBuilder.module(Module1::class, Module2::class)
        val injectionData = injectionBuilder.build()

        assertThat(injectionData.context).isInstanceOf(Context::class.java)
        assertThat(injectionData.modules).isEqualTo(listOf(Module1, Module2))
    }
}
