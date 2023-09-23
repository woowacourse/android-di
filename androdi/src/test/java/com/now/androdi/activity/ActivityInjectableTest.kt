package com.now.androdi.activity

import android.os.Bundle
import com.now.androdi.application.ApplicationInjectable
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import junit.framework.TestCase.assertSame
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

class TestApplication : ApplicationInjectable()

class TestActivity : ActivityInjectable() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
    }
}

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApplication::class)
class ActivityInjectableTest {
    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var testActivity: TestActivity

    @Before
    fun setup() {
        activityController = Robolectric.buildActivity(TestActivity::class.java)
        testActivity = activityController.get()
    }

    @Test
    fun `액티비티가 생성되면 Injector가 초기화된다`() {
        // when
        testActivity = activityController.create().get()

        // then
        assertNotNull(testActivity.injector)
    }

    @Test
    fun `Configration Change가 발생한 경우 같은 injector를 갖는다`() {
        // given
        testActivity = activityController.create().get()
        val originInjector = testActivity.injector

        // when
        activityController.configurationChange()
        val expected = testActivity.injector

        // then
        assertSame(expected, originInjector)
    }

    @Test
    fun `Configration Change발생이 아니라면 다른 injector를 갖는다`() {
        // given
        testActivity = activityController.create().get()
        val originInjector = testActivity.injector

        // when
        activityController.recreate()
        testActivity = activityController.get()
        val expected = testActivity.injector

        // then
        assertNotSame(expected, originInjector)
    }
}
