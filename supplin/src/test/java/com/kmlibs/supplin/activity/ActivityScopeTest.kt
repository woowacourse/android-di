package com.kmlibs.supplin.activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import com.kmlibs.supplin.Injector
import com.kmlibs.supplin.fixtures.android.activity.FakeActivity6
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
class ActivityScopeTest {
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
    fun `supplinInjection should provide the correct dependencies instance to Activities`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity6::class.java)
                .create()
                .get()

        // then
        assertThat(activity.fakeUtil1).isNotNull()
    }

    @Test
    fun `supplinInjection should remove the dependencies instance when the activity is destroyed`() {
        // given & when
        val activity =
            Robolectric
                .buildActivity(FakeActivity6::class.java)
                .create()
                .destroy()
                .get()

        // then
        assertThat(Injector.componentContainers[activity::class]).isNull()
    }
}
