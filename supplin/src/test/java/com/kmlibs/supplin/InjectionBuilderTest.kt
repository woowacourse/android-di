package com.kmlibs.supplin

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.fixtures.android.application.FakeApplication
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InjectionBuilderTest {
    private lateinit var injectionBuilder: InjectionBuilder

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        injectionBuilder = InjectionBuilder()
    }

    @Test
    fun `context and modules should be set correctly`() {
        injectionBuilder.applicationModule(
            ApplicationProvider.getApplicationContext<FakeApplication>(),
            FakeConcreteModule::class,
            FakeRepositoryModule::class,
            FakeDataSourceModule::class,
        )
        val containers = injectionBuilder.build()
        assertThat(containers.size).isEqualTo(1)
    }
}
