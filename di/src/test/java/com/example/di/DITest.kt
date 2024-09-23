package com.example.di

import android.content.Context
import android.os.Bundle
import com.example.di.annotations.ActivityContext
import com.example.di.annotations.ActivityScope
import com.example.di.annotations.ApplicationScope
import com.example.di.annotations.Inject
import com.example.di.annotations.ViewModelScope
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = DITest.TestApplication::class)
class DITest {
    class TestApplication : DIApplication(TestModule) {
        override fun onCreate() {
            super.onCreate()
        }
    }

    interface ApplicationScopeRepository

    class DefaultApplicationScopeRepository : ApplicationScopeRepository

    interface ActivityScopeRepository

    class DefaultActivityScopeRepository : ActivityScopeRepository

    class ActivityScopeDateTimeFormatter(
        context: Context,
    )

    interface ViewModelScopeRepository

    class DefaultViewModelScopeRepository : ViewModelScopeRepository

    object TestModule : AppModule {
        @ApplicationScope
        fun provideSingletonRepository(): ApplicationScopeRepository = DefaultApplicationScopeRepository()

        @ActivityScope
        fun provideActivityScopeRepository(): ActivityScopeRepository = DefaultActivityScopeRepository()

        @ActivityScope
        fun provideDateTimeFormatter(
            @ActivityContext context: Context,
        ): ActivityScopeDateTimeFormatter = ActivityScopeDateTimeFormatter(context)

        @ViewModelScope
        fun provideViewModelScopeRepository(): ViewModelScopeRepository = DefaultViewModelScopeRepository()
    }

    class TestViewModel(
        @Inject val viewModelScopeRepository: ViewModelScopeRepository,
        @Inject val applicationScopeRepository: ApplicationScopeRepository,
    ) : DIViewModel()

    class TestActivity : DIAppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setTheme(com.google.android.material.R.style.Theme_AppCompat_DayNight_NoActionBar)
        }

        @Inject
        lateinit var applicationScopeRepository: ApplicationScopeRepository

        @Inject
        lateinit var activityScopeRepository: ActivityScopeRepository

        @Inject
        lateinit var activityScopeDateTimeFormatter: ActivityScopeDateTimeFormatter
        val testViewModel: TestViewModel by injectViewModels(this)
    }

    private lateinit var activityController: ActivityController<TestActivity>
    private lateinit var activity: TestActivity

    @Before
    fun setUp() {
        activityController = Robolectric.buildActivity(TestActivity::class.java)
    }

    @After
    fun tearDown() {
        activityController.pause().stop().destroy()
    }

    @Test
    fun `Application scope dependency should be injected`() {
        // given
        activity = activityController.create().get()

        // then
        assertThat(activity.applicationScopeRepository::class.java).isEqualTo(DefaultApplicationScopeRepository::class.java)
    }

    @Test
    fun `Activity scope dependency should be injected`() {
        // given
        activity = activityController.create().get()

        // then
        assertThat(activity.activityScopeRepository::class.java).isEqualTo(DefaultActivityScopeRepository::class.java)
    }

    @Test
    fun `Activity scope dependency with context should be injected`() {
        // given
        activity = activityController.create().get()

        // then
        assertThat(activity.activityScopeDateTimeFormatter::class.java).isEqualTo(ActivityScopeDateTimeFormatter::class.java)
    }

    @Test
    fun `ViewModel scope dependency should be injected`() {
        // given
        activity = activityController.create().get()

        // then
        assertThat(activity.testViewModel.viewModelScopeRepository::class.java).isEqualTo(DefaultViewModelScopeRepository::class.java)
    }
}
