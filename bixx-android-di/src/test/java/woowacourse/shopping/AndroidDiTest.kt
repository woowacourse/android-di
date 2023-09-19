package woowacourse.shopping

import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import woowacourse.shopping.ActivityInjectManager.ActivityLifecycle

@RunWith(RobolectricTestRunner::class)
@Config(application = AndroidDiTest.TestApplication::class)
class AndroidDiTest {
    internal class MockObject(context: Context)

    internal class TestApplication : Application() {
        override fun onCreate() {
            super.onCreate()
            val injector = Injector.getSingletonInstance()
            injector.dependencyContainer.addInstance(Context::class, emptyList(), applicationContext)
            injector.factoryContainer.addProvideFactory(SingletonProvider())
        }
    }

    internal class SingletonProvider() {
        @Singleton
        fun provideMockObject(context: Context): MockObject {
            return MockObject(context)
        }
    }

    internal class TestActivity :
        AppCompatActivity(),
        ActivityInjectManager by DefaultActivityInjectManager() {

        @Inject
        @ActivityLifecycle
        lateinit var disposableMock: MockObject

        @Inject
        lateinit var singletonMock: MockObject

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
            registerActivity(this)
            addFactory(Provider())
        }

        class Provider() {
            fun provideMockObject(context: Context): MockObject {
                return MockObject(context)
            }
        }
    }

    @Test
    fun `onCreate가 호출되면 Inject 어노테이션을 가진 액티비티의 프로퍼티들이 주입된다`() {
        // given
        // when
        val activity = Robolectric
            .buildActivity(TestActivity::class.java)
            .create()
            .get()

        // then
        assertNotNull(activity.disposableMock)
    }

    @Test
    fun `액티비티 라이프사이클을 따르는 프로퍼티는 화면 회전에서 살아남지 못한다`() {
        // when
        val activityBeforeRotation = Robolectric
            .buildActivity(TestActivity::class.java)
            .create()
            .get()
        val mockBeforeRotation = activityBeforeRotation.disposableMock

        val activityAfterRotation = Robolectric
            .buildActivity(TestActivity::class.java)
            .recreate()
            .get()
        val mockAfterRotation = activityAfterRotation.disposableMock

        // then
        assertNotSame(mockBeforeRotation, mockAfterRotation)
    }

    @Test
    fun `Provider에서 싱글턴으로 저장된 타입의 프로퍼티는 화면 회전에서도 살아남는다`() {
        // when
        val activityBeforeRotation = Robolectric
            .buildActivity(TestActivity::class.java)
            .create()
            .get()
        val mockBeforeRotation = activityBeforeRotation.singletonMock

        val activityAfterRotation = Robolectric
            .buildActivity(TestActivity::class.java)
            .recreate()
            .get()
        val mockAfterRotation = activityAfterRotation.singletonMock

        // then
        assertEquals(mockBeforeRotation, mockAfterRotation)
    }
}
