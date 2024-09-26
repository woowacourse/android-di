package woowacourse.shopping.di

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    application = FakeApplication::class,
)
class LifecycleDependencyInjectTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var activityController: ActivityController<FakeActicity>
    lateinit var application: FakeApplication

    @Before
    fun setUp() {
        activityController =
            Robolectric.buildActivity(FakeActicity::class.java)
        application = activityController.get().application as FakeApplication
    }

    @Test
    fun `Activity 생명주기를 따르는 의존성은 Activity가 Create된 후 인스턴스화된다`() {
        // when
        val activity = activityController.create().get()

        // then
        assertThat(activity.activityScopeObject).isNotNull()
    }

    @Test
    fun `Activity 생명주기를 따르는 의존성은 Activity가 Destroy된 후 인스턴스가 삭제된다`() {
        // given
        val activity = activityController.create().destroy().get()

        // when
        val instance = activity.dependencyContainer.getInstance<ApplicationScopeObject>(ApplicationScopeObject::class)

        // then
        assertThat(instance).isNull()
    }

    @Test
    fun `ViewModel의 생명주기를 따르는 의존성은 ViewModel이 생성된 후 인스턴스화된다`() {
        // given
        val activity = activityController.create().get()

        // when
        val viewModel = activity.fakeViewModel
        val viewModelScopeInstance = activity.fakeViewModel.viewModelScopeObject

        // then
        assertThat(viewModel).isNotNull()
        assertThat(viewModelScopeInstance).isNotNull()
    }

    @Test
    fun `ViewModel의 생명주기를 따르는 의존성은 ViewModel이 Clear된 후 인스턴스가 삭제된다`() {
        // given
        val activity = activityController.create().destroy().get()

        // when
        val viewModelScopeInstance =
            activity.dependencyContainer.getInstance<ViewModelScopeObject>(ViewModelScopeObject::class)

        // then
        assertThat(viewModelScopeInstance).isNull()
    }

    @Test
    fun `Application의 생명주기를 따르는 의존성은 앱이 종료되기 전까지 인스턴스로 남아있다`() {
        // given
        val activity = activityController.create().get()
        activity.fakeViewModel

        // when
        activityController.destroy()
        val applicationScopeInstance =
            application.applicationDependencyContainer.getInstance<ApplicationScopeObject>(ApplicationScopeObject::class)

        // then
        assertThat(applicationScopeInstance).isNotNull()
    }
}
