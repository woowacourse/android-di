package com.kmlibs.supplin

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.android.application.FakeApplication
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
        injectionBuilder.applicationModule(
            ApplicationProvider.getApplicationContext<FakeApplication>(),
            FakeConcreteModule::class,
            FakeRepositoryModule::class,
            FakeDataSourceModule::class
        )
        val injectionData = injectionBuilder.build()
    }
}
