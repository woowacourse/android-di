package com.example.yennydi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.yennydi.fixture.FakeActivity
import com.example.yennydi.fixture.FakeApplication
import com.example.yennydi.fixture.FakeInjectedComponent
import com.example.yennydi.fixture.FakeRepository
import com.example.yennydi.fixture.FakeRepositoryImpl
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class LifeCycleTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var activityController: ActivityController<FakeActivity>
    private lateinit var activity: FakeActivity
    private lateinit var application: FakeApplication

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(FakeActivity::class.java)

        activity = activityController.get()

        application = activity.application as FakeApplication
    }

    @Test
    fun `Application이 생성되면 Application LifeCycle 동안 유지되는 인스턴스가 존재한다`() {
        val target = FakeRepositoryImpl()

        val result: FakeRepository? = application.instanceContainer.getInstance(target::class)

        assertThat(result).isInstanceOf(FakeRepository::class.java)
    }

    @Test
    fun `Application이 종료되면 Application LifeCycle를 가진 인스턴스가 삭제된다`() {
        application.onTerminate()

        val target = FakeRepositoryImpl()
        val result: FakeRepository? = application.instanceContainer.getInstance(target::class)

        assertThat(result).isNull()
    }

    @Test
    fun `Activity가 생성되면 Activity LifeCycle 를 가진 인스턴스가 생성된다`() {
        activity = activityController.create().get()

        val target = FakeInjectedComponent()
        val result: FakeInjectedComponent? = activity.instanceContainer.getInstance(target::class)

        assertThat(result).isInstanceOf(FakeInjectedComponent::class.java)
    }

    @Test
    fun `Activity가 종료되면 Activity LifeCycle 를 가진 인스턴스들이 삭제된다`() {
        activity = activityController.create().get()

        activityController.destroy()

        val target = FakeInjectedComponent()
        val result: FakeInjectedComponent? = activity.instanceContainer.getInstance(target::class)
        assertThat(result).isNull()
    }
}
