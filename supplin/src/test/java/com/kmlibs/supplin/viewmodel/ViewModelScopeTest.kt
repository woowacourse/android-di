package com.kmlibs.supplin.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity7
import com.kmlibs.supplin.fixtures.android.application.FakeApplication
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        Injector.setModules {
            applicationModule(
                ApplicationProvider.getApplicationContext<FakeApplication>(),
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }
    }

    @After
    fun tearDown() {
        Injector.setModules {
            removeAllModules()
        }
    }

    @Test
    fun `supplin viewmodel should inject correct dependencies according to viewmodel lifecycle`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity7::class.java)
                .create()
                .get()

        // then
        assertThat(activity.fakeViewModel.fakeUtil2).isNotNull()
    }

    @Test
    fun `dependencies in supplin viewmodel scope should be removed after viewmodel is cleared`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity7::class.java)
                .create()
                .destroy()
                .get()

        // then
        assertThat(Injector.componentContainers[activity.fakeViewModel::class]).isNull()
    }
}
