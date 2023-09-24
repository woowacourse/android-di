package com.woowacourse.shopping

import android.content.Context
import android.os.Bundle
import com.woowacourse.shopping.annotation.Retain
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import woowacourse.shopping.otterdi.annotation.Inject
import java.lang.IllegalStateException

@RunWith(RobolectricTestRunner::class)
class OtterDiActivityTest {

    class FakeDependency()

    class FakeModule : AndroidModule {
        override var context: Context? = null

        fun provideFakeDependency(): FakeDependency = FakeDependency()
    }

    class FakeActivity : OtterDiActivity(FakeModule()) {
        @Inject
        lateinit var fakeDependency: FakeDependency

        @Inject
        @Retain
        lateinit var retainDependency: FakeDependency

        @Inject
        lateinit var notRetainDependency: FakeDependency

        var notInitializeDependency: FakeDependency? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setTheme(androidx.appcompat.R.style.Theme_AppCompat_Light_NoActionBar)
        }
    }

    class FakeOtterDiApplication() : OtterDiApplication(FakeModule())

    // ----------------------------------------------------------------------------

    private lateinit var activity: ActivityController<FakeActivity>

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(FakeActivity::class.java)
    }

    @Test
    fun `Inject 어노테이션이 있는 변수에 의존성을 주입한다`() {
        // when
        activity.setup()

        // then
        val dependency = activity.get().fakeDependency
        assertNotNull(dependency)
    }

    @Test(expected = UninitializedPropertyAccessException::class)
    fun `onCreate()가 호출되기 이전에는 변수가 초기화되지 않는다`() {
        // then
        activity.get().fakeDependency
    }

    @Test
    fun `onCreate() 호출 이후에 변수를 초기화한다`() {
        // when
        activity.create()

        // then
        val dependency = activity.get().fakeDependency
        assertNotNull(dependency)
    }

    @Test(expected = IllegalStateException::class)
    fun `onDestroy() 호출 이후에 변수는 해제된다`() {
        // when
        activity.destroy()

        // then
        activity.get().fakeDependency
    }

    @Test
    fun `Inject 어노테이션이 없는 변수에는 의존성을 주입하지 않는다`() {
        // when
        activity.setup()

        // then
        val dependency = activity.get().notInitializeDependency
        assertNull(dependency)
    }

    @Test
    fun `Retain 어노테이션이 있는 변수는 구성 변경에도 살아남는다`() {
        // when
        activity.setup()
        val firstDependency = activity.get().retainDependency

        activity.configurationChange()
        val secondDependency = activity.get().retainDependency

        // then
        assertEquals(firstDependency, secondDependency)
    }

    @Test
    fun `Retain 어노테이션이 없는 변수는 구성 변경 이후 새롭게 초기화된다`() {
        // when
        activity.setup()
        val firstDependency = activity.get().notRetainDependency

        activity.configurationChange()
        val secondDependency = activity.get().notRetainDependency

        // then
        assertEquals(firstDependency, secondDependency)
    }
}
