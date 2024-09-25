package com.kmlibs.supplin

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.kmlibs.supplin.fixtures.android.application.FakeApplication
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InjectorTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @After
    fun tearDown() {
        Injector.setModules {
            removeAllModules()
        }
    }

    @Test
    fun `Injector should be initialized by explicitly calling init`() {
        Injector.setModules {
            applicationModule(
                ApplicationProvider.getApplicationContext<FakeApplication>(),
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }
        Injector.componentContainers
    }

    @Test(expected = IllegalArgumentException::class)
    fun `InstanceContainer in Injector cannot be initialized repeatedly`() {
        // when
        Injector.setModules {
            applicationModule(
                ApplicationProvider.getApplicationContext<FakeApplication>(),
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }

        Injector.setModules {
            applicationModule(
                ApplicationProvider.getApplicationContext<FakeApplication>(),
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }
    }
}
