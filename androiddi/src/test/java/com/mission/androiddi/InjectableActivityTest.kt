package com.mission.androiddi

import android.os.Build
import android.os.Bundle
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.component.application.InjectableApplication
import com.mission.androiddi.scope.ActivityScope
import com.woowacourse.bunadi.annotation.Inject
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

/**
 * 테스트용 Fake 클래스
 * */

class TestDependency {
    private var value = 0

    fun increase(): Int = ++value
}

class TestActivity : InjectableActivity() {
    override val activityClazz = TestActivity::class

    @Inject
    @ActivityScope
    lateinit var notDestroyDependency: TestDependency

    val destroyDependency: TestDependency = TestDependency()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
    }
}

/**
 * ################################################################################################
 * */

/**
 * 테스트 코드
 * */

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.P],
    application = InjectableApplication::class,
)
class InjectableActivityTest {
    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var activity: TestActivity

    @Before
    fun setup() {
        activityController = Robolectric
            .buildActivity(TestActivity::class.java)
            .create()
        activity = activityController.get()
    }

    @Test
    fun 구성_변경이_발생했을_때_액티비티가_파괴되어도_주입받는_객체는_제거되지_않는다() {
        // given: 주입받는 객체를 생성한다.
        val value = activity.notDestroyDependency.increase()
        val before = activity.notDestroyDependency

        // when: 구성 변경이 발생한다.
        activityController.recreate()
        activity = activityController.get()

        // then: notDestroyDependency 객체는 파괴되지 않으므로, 값과 객체가 동일하다.
        val actual = activity.notDestroyDependency.increase()
        val expected = value * 2
        val after = activity.notDestroyDependency

        assert(actual == expected)
        assertTrue(before === after)
    }

    @Test
    fun 구성_변경이_발생했을_때_액티비티가_파괴되면_주입받지_않았던_객체는_제거된다() {
        // given: 주입받는 객체를 생성한다.
        val value = activity.destroyDependency.increase()
        val before = activity.destroyDependency

        // when: 구성 변경이 발생한다.
        activityController.recreate()
        activity = activityController.get()

        // then: destroyDependency 객체는 파괴되므로, 값을 증가시켰을 때 원래 값 그대로이다.
        val actual = activity.destroyDependency.increase()
        val after = activity.destroyDependency

        assert(actual == value)
        assertFalse(before == after)
    }
}
