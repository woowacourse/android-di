package com.kmlibs.supplin

import android.content.Context
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

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
        injectionBuilder.module(FakeConcreteModule::class, FakeRepositoryModule::class, FakeDataSourceModule::class)
        val injectionData = injectionBuilder.build()

        assertThat(injectionData.context).isInstanceOf(Context::class.java)
        assertThat(injectionData.modules).containsExactly(FakeConcreteModule::class, FakeRepositoryModule::class, FakeDataSourceModule::class)
    }
}
