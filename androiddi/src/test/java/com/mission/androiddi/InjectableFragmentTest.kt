package com.mission.androiddi

import android.os.Build
import android.os.Bundle
import com.mission.androiddi.component.activity.InjectableActivity
import com.mission.androiddi.component.application.InjectableApplication
import com.mission.androiddi.component.fragment.InjectableFragment
import com.mission.androiddi.scope.ActivityScope
import com.mission.androiddi.scope.FragmentScope
import com.mission.androiddi.scope.RetainActivityScope
import com.woowacourse.bunadi.annotation.Inject
import com.woowacourse.bunadi.injector.DependencyKey
import io.mockk.mockk
import junit.framework.TestCase.assertNull
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

internal class RetainSharedResource {
    private var value = 0

    fun increase(): Int = ++value
}

internal class NotRetainSharedResource {
    private var value = 0

    fun increase(): Int = ++value
}

internal class FragmentOnlyResource {
    private var value = 0

    fun increase(): Int = ++value
}

internal class ActivityWithFragment : InjectableActivity() {
    override val activityClazz = ActivityWithFragment::class

    @Inject
    @RetainActivityScope
    lateinit var retainDependency: RetainSharedResource

    @Inject
    @ActivityScope
    lateinit var notRetainDependency: NotRetainSharedResource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(androidx.appcompat.R.style.Theme_AppCompat_DayNight_NoActionBar)
        addChildFragment()
    }

    private fun addChildFragment() {
        supportFragmentManager.beginTransaction()
            .add(ChildFragment(), ChildFragment::class.simpleName)
            .commitNow()
    }
}

internal class ChildFragment : InjectableFragment() {
    override val fragmentClazz = ChildFragment::class

    @Inject
    lateinit var retainSharedResource: RetainSharedResource

    @Inject
    lateinit var notRetainSharedResource: NotRetainSharedResource

    @Inject
    @FragmentScope
    lateinit var fragmentOnlyResource: FragmentOnlyResource

    var notInjectedDependency: NotRetainSharedResource? = null
}

/**
 * =================================== Fragment Inject Test ========================================
 * */

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.P],
    application = InjectableApplication::class,
)
class InjectableFragmentTest {
    private lateinit var activityController: ActivityController<ActivityWithFragment>
    private lateinit var activity: ActivityWithFragment

    @Before
    fun setup() {
        activityController = Robolectric
            .buildActivity(ActivityWithFragment::class.java)
        activity = activityController.get()
    }

    @Test
    fun `프래그먼트는 부모 액티비티의 ActivityScope 애노테이션 프로퍼티를 공유한다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragment = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment

        // when: 자식 프래그먼트의 onViewCreated()가 호출될 때
        fragment.onViewCreated(mockk(), mockk())
        val sharedResource = fragment.notRetainSharedResource

        // then: 자식 프래그먼트는 부모 액티비티의 ActivityScope 애노테이션이 붙은 프로퍼티를 공유한다.
        assertTrue(activity.notRetainDependency === sharedResource)
    }

    @Test
    fun `부모 액티비티의 RetainActivityScope 애노테이션 프로퍼티는 구성 변경이 일어나도 프래그먼트에서도 유지된다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragment = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment
        fragment.onViewCreated(mockk(), mockk())

        val valueBeforeConfigurationChange =
            fragment.retainSharedResource.increase() // 1

        // when: 구성 변경이 일어났을 때
        activityController.recreate()
        activity = activityController.get()

        val valueAfterConfigurationChange = fragment.retainSharedResource.increase() // 2

        // then: RetainActivityScope 애노테이션이 있는 프로퍼티는 유지된다.
        assertTrue(valueBeforeConfigurationChange == 1)
        assertTrue(valueAfterConfigurationChange == 2)
        assertTrue(activity.retainDependency.increase() == 3)
    }

    @Test
    fun `FragmentScope 애노테이션 프로퍼티는 구성 변경이 일어나면 유지되지 않는다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragmentBeforeConfigurationChange = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment
        fragmentBeforeConfigurationChange.onViewCreated(mockk(), mockk())

        val valueBeforeConfigurationChange = fragmentBeforeConfigurationChange
            .fragmentOnlyResource
            .increase() // 1

        // when: 구성 변경이 일어났을 때
        activityController.recreate()
        activity = activityController.get()
        val fragmentAfterConfigurationChange = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment
        fragmentAfterConfigurationChange.onViewCreated(mockk(), mockk())

        val valueAfterConfigurationChange = fragmentAfterConfigurationChange
            .fragmentOnlyResource
            .increase() // 1

        // then: FragmentScope 애노테이션이 있는 프로퍼티는 유지되지 않는다.
        assertTrue(valueBeforeConfigurationChange == 1)
        assertTrue(valueAfterConfigurationChange == 1)
    }

    @Test
    fun `부모 액티비티의 ActivityScope 애노테이션을 가진 프로퍼티는 구성 변경이 일어나면 유지되지 않는다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragmentBeforeConfigurationChange = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment
        fragmentBeforeConfigurationChange.onViewCreated(mockk(), mockk())

        val valueBeforeConfigurationChange = fragmentBeforeConfigurationChange
            .notRetainSharedResource
            .increase() // 1

        // when: 구성 변경이 일어났을 때
        activityController.recreate()
        activity = activityController.get()
        val fragmentAfterConfigurationChange = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment
        fragmentAfterConfigurationChange.onViewCreated(mockk(), mockk())

        val valueAfterConfigurationChange = fragmentAfterConfigurationChange
            .notRetainSharedResource
            .increase() // 1

        // then: ActivityScope 애노테이션이 있는 프로퍼티는 유지되지 않는다.
        assertTrue(valueBeforeConfigurationChange == 1)
        assertTrue(valueAfterConfigurationChange == 1)
    }

    @Test
    fun `FragmentScope 애노테이션을 가진 프로퍼티는 프래그먼트 인젝터의 캐시에 캐싱된다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragment = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment

        // when: 자식 프래그먼트의 onViewCreated()가 호출될 때
        fragment.onViewCreated(mockk(), mockk())

        // then: 프래그먼트 인젝터의 캐시에 캐싱된다
        val fragmentCache = fragment.cache
        val expected = fragment.fragmentOnlyResource
        val actual = fragmentCache[DependencyKey.createDependencyKey(FragmentOnlyResource::class)]
        assertTrue(actual === expected)
    }

    @Test
    fun `Inject 애노테이션이 없는 프로퍼티는 주입받지 않는다`() {
        // given: 부모 액티비티의 onCreate()가 호출된다
        activityController.create()
        activity = activityController.get()

        // given: 부모 액티비티의 프래그먼트를 가져온다.
        val fragment = activity
            .supportFragmentManager
            .findFragmentByTag(ChildFragment::class.simpleName) as ChildFragment

        // when: 자식 프래그먼트의 onViewCreated()가 호출될 때
        fragment.onViewCreated(mockk(), mockk())

        // then: Inject 애노테이션이 없는 프로퍼티는 주입받지 않는다.
        assertNull(fragment.notInjectedDependency)
    }
}
