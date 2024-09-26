package com.zzang.di

import com.google.common.truth.Truth.assertThat
import com.zzang.di.testfixture.FakeActivity
import com.zzang.di.testfixture.FakeApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = FakeApplication::class)
class ActivityLifeCycleTest {
    private lateinit var controller: ActivityController<FakeActivity>
    private lateinit var activity: FakeActivity

    @Before
    fun setUp() {
        DIContainer.clearAll()
        controller = Robolectric.buildActivity(FakeActivity::class.java)
        activity = controller.get()
    }

    @Test
    fun `Activity가 onCreate될 때 fakeService가 주입된다`() {
        // WHEN
        controller.create()

        // THEN
        assertThat(activity.fakeService).isNotNull()
    }

    @Test
    fun `Activity가 onDestroy될 때 fakeService가 제거된다`() {
        // GIVEN
        controller.create()

        // WHEN
        controller.destroy()

        // THEN
        assertThat(DIContainer.activityScopedInstanceSize()).isEqualTo(0)
    }

    @Test
    fun `Activity가 onStart될 때 fakeService가 유지된다`() {
        // GIVEN
        controller.create()
        val originalFakeService = activity.fakeService

        // WHEN
        controller.start()

        // THEN
        assertThat(activity.fakeService).isSameInstanceAs(originalFakeService)
    }

    @Test
    fun `Activity가 onResume될 때 fakeService가 유지된다`() {
        // GIVEN
        controller.create()
        val originalFakeService = activity.fakeService

        // WHEN
        controller.resume()

        // THEN
        assertThat(activity.fakeService).isSameInstanceAs(originalFakeService)
    }

    //onPause
    @Test
    fun `Activity가 onPause될 때 fakeService가 유지된다`() {
        // GIVEN
        controller.create()
        val originalFakeService = activity.fakeService

        // WHEN
        controller.pause()

        // THEN
        assertThat(activity.fakeService).isSameInstanceAs(originalFakeService)
    }

    //onRestart
    @Test
    fun `Activity가 onRestart될 때 fakeService가 유지된다`() {
        // GIVEN
        controller.create()
        val originalFakeService = activity.fakeService

        // WHEN
        controller.restart()

        // THEN
        assertThat(activity.fakeService).isSameInstanceAs(originalFakeService)
    }

    @Test
    fun `Activity가 onStop될 때 fakeService가 유지된다`() {
        // GIVEN
        controller.create()
        val originalFakeService = activity.fakeService

        // WHEN
        controller.stop()

        // THEN
        assertThat(activity.fakeService).isSameInstanceAs(originalFakeService)
    }
}

