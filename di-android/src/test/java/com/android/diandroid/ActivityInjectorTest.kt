package com.android.diandroid

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ActivityInjectorTest {
    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var testActivity: TestActivity

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(TestActivity::class.java)
        testActivity = activityController.get()
    }

    @Test
    fun `액티비티가 생성되면 di를 주입하는 diInjector가 생성되어야 한다`() {
        //when
        testActivity = activityController.create().get()

        //then
        assert(testActivity.diInjector != null)
    }

    @Test
    fun `Configration Change 시 기존에 생성한 diInjector를 활용한다`() {
        // given
        testActivity = activityController.create().get()
        val originalInjector = testActivity.diInjector

        // when
        activityController.configurationChange()
        val expectedInjector = testActivity.diInjector

        // then
        assert(expectedInjector == originalInjector)
    }

    @Test
    fun `액티비티가 종료될 때 diInjector가 제거되어야 한다`() {
        // given
        testActivity = activityController.create().get()

        // when
        activityController.destroy()

        // then
        val app = testActivity.application as TestApplication
        assert(app.getInjector(testActivity.javaClass.name) == null)
    }
}
