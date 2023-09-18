package com.mission.androiddi

import android.os.Build
import android.os.Bundle
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.component.application.InjectableApplication
import com.mission.androiddi.scope.ActivityScope
import com.mission.androiddi.scope.RetainActivityScope
import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.injector.DependencyKey
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
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

class RetainDependency {
    private var value = 0

    fun increase(): Int = ++value
}

class NotRetainDependency {
    private var value = 0

    fun increase(): Int = ++value
}

class TestActivity : InjectableActivity() {
    override val activityClazz = TestActivity::class

    @Inject
    @RetainActivityScope
    lateinit var retainDependency: RetainDependency

    @Inject
    @ActivityScope
    lateinit var notRetainedDependency: NotRetainDependency

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
        activity = activityController.get()
    }

    /**
     * ===================================ActivityScope Test========================================
     * */

    @Test
    fun ActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_CREATED_상태일_때_인스턴스가_존재한다() {
        // given: NotRetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(NotRetainDependency::class)

        // when: 액티비티가 CREATED 상태일 때
        activity = activityController
            .create()
            .get()

        // then: ActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun ActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_STARTED_상태일_때_인스턴스가_존재한다() {
        // given: NotRetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(NotRetainDependency::class)

        // when: 액티비티가 STARTED 상태일 때
        activity = activityController
            .create()
            .start()
            .get()

        // then: ActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun ActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_RESUMED_상태일_때_인스턴스가_존재한다() {
        // given: NotRetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(NotRetainDependency::class)

        // when: 액티비티가 RESUMED 상태일 때
        activity = activityController
            .create()
            .start()
            .resume()
            .get()

        // then: ActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun ActivityScope_애노테이션이_있는_프로퍼티는_구성_변경이_발생했을_때_제거된다() {
        // given: 액티비티를 생성한다.
        activityController
            .create()
            .start()
            .resume()
        activity = activityController.get()

        val value = activity.notRetainedDependency.increase()
        val before = activity.notRetainedDependency

        // when: 구성 변경이 발생한다.
        activityController.recreate()
        activity = activityController.get()

        // then: destroyDependency 객체는 파괴되므로, 값을 증가시켰을 때 원래 값 그대로이다.
        val actual = activity.notRetainedDependency.increase()
        val after = activity.notRetainedDependency

        assert(actual == value)
        assertFalse(before == after)
    }

    /**
     * ================================RetainActivityScope Test=====================================
     * */

    @Test
    fun RetainActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_CREATED_상태일_때_인스턴스가_존재한다() {
        // given: RetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(RetainDependency::class)

        // when: 액티비티가 CREATED 상태일 때
        activity = activityController
            .create()
            .get()

        // then: RetainActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun RetainActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_STARTED_상태일_때_인스턴스가_존재한다() {
        // given: RetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(RetainDependency::class)

        // when: 액티비티가 STARTED 상태일 때
        activity = activityController
            .create()
            .start()
            .get()

        // then: RetainActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun RetainActivityScope_애노테이션이_있는_프로퍼티는_액티비티가_RESUMED_상태일_때_인스턴스가_존재한다() {
        // given: RetainDependency 키가 주어진다.
        val dependencyKey = DependencyKey.createDependencyKey(RetainDependency::class)

        // when: 액티비티가 RESUMED 상태일 때
        activity = activityController
            .create()
            .start()
            .resume()
            .get()

        // then: RetainActivityScope 애노테이션이 붙은 프로퍼티는 인스턴스가 존재한다.
        val actual = activity.cache[dependencyKey]
        assertNotNull(actual)
    }

    @Test
    fun RetainActivityScope_애노테이션이_있는_프로퍼티는_구성_변경이_발생했을_때_제거되지_않는다() {
        // given: 액티비티를 생성한다.
        activityController
            .create()
            .start()
            .resume()
        activity = activityController.get()

        val value = activity.retainDependency.increase()
        val before = activity.retainDependency

        // when: 구성 변경이 발생한다.
        activityController.recreate()
        activity = activityController.get()

        // then: notDestroyDependency 객체는 파괴되지 않으므로, 값과 객체가 동일하다.
        val actual = activity.retainDependency.increase()
        val expected = value * 2
        val after = activity.retainDependency

        assert(actual == expected)
        assertTrue(before === after)
    }
}
