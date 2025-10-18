package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.core.di.DependencyContainer

@RunWith(RobolectricTestRunner::class)
class AndroidContainerTest {
    @Before
    fun resetAllContainers() {
        val appContainerField = AndroidContainer::class.java.getDeclaredField("appContainer")
        appContainerField.isAccessible = true
        val appContainer = appContainerField.get(AndroidContainer) as DependencyContainer

        val dependenciesField = DependencyContainer::class.java.getDeclaredField("dependencies")
        dependenciesField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val dependencies = dependenciesField.get(appContainer) as MutableMap<*, *>
        dependencies.clear()

        val activityContainersField =
            AndroidContainer::class.java.getDeclaredField("activityContainers")
        activityContainersField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val activityContainers = activityContainersField.get(AndroidContainer) as MutableMap<*, *>
        activityContainers.clear()

        val viewModelContainersField =
            AndroidContainer::class.java.getDeclaredField("viewModelContainers")
        viewModelContainersField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val viewModelContainers = viewModelContainersField.get(AndroidContainer) as MutableMap<*, *>
        viewModelContainers.clear()
    }

    @Test
    fun `ofApplication은 항상 동일한 인스턴스를 반환한다`() {
        // given
        val firstApplicationContainer = AndroidContainer.ofApplication()
        val secondApplicationContainer = AndroidContainer.ofApplication()

        // then
        assertThat(firstApplicationContainer).isSameInstanceAs(secondApplicationContainer)
    }

    @Test
    fun `ofActivity는 Activity마다 서로 다른 컨테이너를 반환한다`() {
        // given
        val firstActivity = Robolectric.buildActivity(TestActivity::class.java).create().get()
        val secondActivity = Robolectric.buildActivity(TestActivity::class.java).create().get()

        val firstActivityContainer = AndroidContainer.ofActivity(firstActivity)
        val secondActivityContainer = AndroidContainer.ofActivity(secondActivity)

        // then
        assertThat(firstActivityContainer).isNotSameInstanceAs(secondActivityContainer)
    }

    @Test
    fun `clear를 호출하면 해당 Activity 컨테이너가 제거된다`() {
        // given
        val activity = Robolectric.buildActivity(TestActivity::class.java).create().get()
        val activityContainerBeforeClear = AndroidContainer.ofActivity(activity)

        // when
        AndroidContainer.clear(activity)
        val activityContainerAfterClear = AndroidContainer.ofActivity(activity)

        // then
        assertThat(activityContainerBeforeClear).isNotSameInstanceAs(activityContainerAfterClear)
    }

    @Test
    fun `ofViewModel은 ViewModel마다 서로 다른 컨테이너를 반환한다`() {
        // given
        val firstViewModel = TestViewModel()
        val secondViewModel = TestViewModel()

        val firstViewModelContainer = AndroidContainer.ofViewModel(firstViewModel)
        val secondViewModelContainer = AndroidContainer.ofViewModel(secondViewModel)

        // then
        assertThat(firstViewModelContainer).isNotSameInstanceAs(secondViewModelContainer)
    }

    @Test
    fun `clear를 호출하면 해당 ViewModel 컨테이너가 제거된다`() {
        // given
        val viewModel = TestViewModel()
        val viewModelContainerBeforeClear = AndroidContainer.ofViewModel(viewModel)

        // when
        AndroidContainer.clear(viewModel)
        val viewModelContainerAfterClear = AndroidContainer.ofViewModel(viewModel)

        // then
        assertThat(viewModelContainerBeforeClear).isNotSameInstanceAs(viewModelContainerAfterClear)
    }

    class TestActivity : ComponentActivity()

    class TestViewModel : ViewModel()
}
