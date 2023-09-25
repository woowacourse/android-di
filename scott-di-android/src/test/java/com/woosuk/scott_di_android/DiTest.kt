package com.woosuk.scott_di_android

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import junit.framework.TestCase.assertEquals
import org.junit.After
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

@RunWith(RobolectricTestRunner::class)
@Config(application = DiTest.TestApplication::class)
class DiTest {

    // Application이 실행되면 Singleton 의존성 객체들을 DiContainer에 저장한다.
    class TestApplication() : DiApplication(TestModule) {
        override fun onCreate() {
            super.onCreate()
        }
    }

    interface SingletonRepo
    class DefaultSingletonRepo : SingletonRepo

    interface ActivityScopeRepo
    class DefaultActivityScopeRepo : ActivityScopeRepo

    class ActivityScopeDateTimeFormatter(context: Context)

    interface ViewModelScopeRepo
    class DefaultViewModelScopeRepo : ViewModelScopeRepo

    object TestModule : Module {

        @Singleton
        fun provideSingletonRepo(): SingletonRepo {
            return DefaultSingletonRepo()
        }

        @ActivityScope
        fun provideActivityScopeRepo(): ActivityScopeRepo {
            return DefaultActivityScopeRepo()
        }

        @ActivityScope
        fun provideDateTimeFormatter(
            @ActivityContext context: Context,
        ): ActivityScopeDateTimeFormatter {
            return ActivityScopeDateTimeFormatter(context)
        }

        @ViewModelScope
        fun provideViewModelScopeRepo(): ViewModelScopeRepo {
            return DefaultViewModelScopeRepo()
        }
    }

    class TestViewModel(
        @Inject val viewModelScopeRepo: ViewModelScopeRepo,
        @Inject val singletonRepo: SingletonRepo,
    ) : DiViewModel()

    class TestActivity() : DiAppComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
        }

        @Inject
        lateinit var singletonRepo: SingletonRepo

        @Inject
        lateinit var activityScopeRepo: ActivityScopeRepo

        @Inject
        lateinit var activityScopeDateTimeFormatter: ActivityScopeDateTimeFormatter

        val testViewModel: TestViewModel by lazy {
            ViewModelProvider(
                this,
                ViewModelFactoryUtil.viewModelFactory<TestViewModel>()
            )[TestViewModel::class.java]
        }
    }

    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var activity: TestActivity

    @Before
    fun setup() {
        activityController = Robolectric
            .buildActivity(TestActivity::class.java)
    }

    @After
    fun tearDown() {
        activityController.close()
    }

    /**
     * ================================긍정 테스트 구간 입니다=====================================
     * */

    @Test
    fun `@Singleton이 붙은 의존성 객체는 Acitivity 에서 필드 주입이 가능하다`() {
        // when
        activity = activityController
            .create()
            .get()
        // then
        assertEquals(activity.singletonRepo::class.java, DefaultSingletonRepo::class.java)
    }

    @Test
    fun `@Singleton이 붙은 의존성 객체는 Acitivity가 회전하더라도 유지된다`() {
        // when
        val notRotatedActivity = activityController
            .create()
            .get()
        val notRotationObject = notRotatedActivity.singletonRepo

        val rotatedActivity = activityController
            .recreate()
            .get()
        val rotatedObject = rotatedActivity.singletonRepo
        // then
        assertEquals(notRotationObject, rotatedObject)
    }

    @Test
    fun `@ActivityScope가 붙은 의존성 객체를 Activity에서 필드주입이 가능하다`() {
        // when
        activity = activityController
            .create()
            .get()
        // then
        assertEquals(activity.activityScopeRepo::class.java, DefaultActivityScopeRepo::class.java)
    }

    @Test
    fun `@ActivityScope와, @ActivityContext 가 붙은 의존성 객체는 Activtiy에서 context 주입을하고, 객체를 Activity에 주입이 가능하다`() {
        // when
        activity = activityController
            .create()
            .get()
        // then
        assertEquals(
            activity.activityScopeDateTimeFormatter::class.java,
            ActivityScopeDateTimeFormatter::class.java
        )
    }

    @Test
    fun `Activity에서 @Inject 가 붙은 객체는 onDestroy 일 때 해제한다`() {
        // when
        activity = activityController
            .create()
            .destroy()
            .get()
        val destroyed = activity::class
            .memberProperties
            .first { it.name == "activityScopeDateTimeFormatter" }
            .javaField
            ?.get(activity)
        // then
        assertThat(destroyed).isNull()
    }


    @Test
    fun `@ActivityScope가 붙은 의존성 객체는 Acitivity가 회전하면 다른 객체를 주입한다`() {
        // when
        val notRotatedActivity = activityController
            .create()
            .get()
        val notRotationObject = notRotatedActivity.activityScopeRepo

        val rotatedObject = activityController.recreate().get().activityScopeRepo
        // then
        assertNotEquals(notRotationObject, rotatedObject)
    }

    /**
     * ================================ViewModel 테스트 구간입니다=====================================
     * */

    @Test
    fun `ViewModel을 생성할 때, Singleton으로 정의된 의존성 객체 주입이 가능하다`() {
        // when
        val activity = activityController
            .create()
            .get()
        val testViewModel = activity.testViewModel

        // then
        assertNotNull(testViewModel.singletonRepo)
    }

    @Test
    fun `ViewModel을 생성할 때, ViewModelScope로 정의된 의존성 객체 주입이 가능하다`() {
        // when
        val activity = activityController
            .create()
            .get()
        val testViewModel = activity.testViewModel

        // then
        assertNotNull(testViewModel.viewModelScopeRepo)
    }

    @Test
    fun `ViewModel 에서 Singleton으로 정의된 의존성 객체는, 엑티비티가 회전되어도 파괴되지 않는다`() {
        // when
        val activity = activityController
            .create()
            .get()
        val nonRotatedSingletonRepo = activity.testViewModel.singletonRepo

        val rotatedActivity = activityController
            .recreate()
            .get()
        val rotatedSingletonRepo = rotatedActivity.testViewModel.singletonRepo

        // then
        assertEquals(nonRotatedSingletonRepo, rotatedSingletonRepo)
    }

    @Test
    fun `ViewModel에서 ViewModelScope로 정의된 의존성 객체는, 엑티비티가 회전되어도 파괴되지 않는다`() {
        // when
        val activity = activityController
            .create()
            .get()
        val nonRotatedViewModelScopeRepo = activity.testViewModel.viewModelScopeRepo

        val rotatedActivity = activityController
            .recreate()
            .get()
        val rotatedViewModelScopeRepo = rotatedActivity.testViewModel.viewModelScopeRepo

        // then
        assertEquals(nonRotatedViewModelScopeRepo, rotatedViewModelScopeRepo)
    }

    /**
     * ================================에러 테스트 구간 입니다=====================================
     * */

    @Test
    fun `Activity 에서 ViewModelScope 의존성 객체를 주입할 수 없다`() {
        // given
        class ErrorTestActivity() : DiAppComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
            }

            @Inject
            lateinit var viewModelScopeRepo: ViewModelScopeRepo
        }
        // when
        val errorTestActivityController = Robolectric
            .buildActivity(ErrorTestActivity::class.java)

        // then
        try {
            errorTestActivityController.create().get()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("해당하는 의존성을 찾을 수 없습니다")
        }
    }

    @Test
    fun `ViewModel에서 ActivtiyScope 의존성을 주입할 수 없다`() {
        // given
        class ErrorTestViewModel(
            @Inject val activityScopeRepo: ActivityScopeRepo
        ) : DiViewModel()

        class ErrorTestActivity() : DiAppComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
            }

            val errorTestViewModel: ErrorTestViewModel by lazy {
                ViewModelProvider(
                    this,
                    ViewModelFactoryUtil.viewModelFactory<ErrorTestViewModel>()
                )[ErrorTestViewModel::class.java]
            }
        }
        // when
        val errorTestActivityController = Robolectric
            .buildActivity(ErrorTestActivity::class.java)

        // then
        try {
            errorTestActivityController.create().get()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("해당하는 의존성을 찾을 수 없습니다")
        }
    }

    @Test
    fun `의존성을 정의하지 않은 객체를 주입하려는 것은 불가능하다`() {
        // given
        class ErrorTestActivity() : DiAppComponentActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
            }

            @Inject
            lateinit var notDefinedString: String
        }
        // when
        val errorTestActivityController = Robolectric
            .buildActivity(ErrorTestActivity::class.java)

        // then
        try {
            errorTestActivityController.create().get()
        } catch (e: Exception) {
            assertThat(e).hasMessageThat().contains("해당하는 의존성을 찾을 수 없습니다")
        }
    }
}
