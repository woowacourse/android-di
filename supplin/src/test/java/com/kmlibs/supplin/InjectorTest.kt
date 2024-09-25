package com.kmlibs.supplin

import androidx.test.core.app.ApplicationProvider
import com.kmlibs.supplin.fixtures.android.application.FakeApplication
import com.kmlibs.supplin.fixtures.android.module.FakeConcreteModule
import com.kmlibs.supplin.fixtures.android.module.FakeDataSourceModule
import com.kmlibs.supplin.fixtures.android.module.FakeRepositoryModule
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class InjectorTest {
    private lateinit var application: FakeApplication

    @Before
    fun setUp() {
        application = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun `Injector should be initialized by explicitly calling init`() {
        Injector.setModules {
            applicationModule(
                application,
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }
        Injector.componentContainers
    }

    @Test
    fun `InstanceContainer in Injector cannot be initialized repeatedly`() {
        // when
        Injector.setModules {
            applicationModule(
                application,
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }

        Injector.setModules {
            applicationModule(
                application,
                FakeConcreteModule::class,
                FakeRepositoryModule::class,
                FakeDataSourceModule::class,
            )
        }

        // then
    }
}
