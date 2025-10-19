package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AndroidContainerScopeTest {
    @Test
    fun `ApplicationScope 인스턴스는 앱 전역에서 유지된다`() {
        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ApplicationScope,
        ) { Any() }

        val first = AndroidContainer.instance(Any::class, Scope.ApplicationScope)
        val second = AndroidContainer.instance(Any::class, Scope.ApplicationScope)

        assertThat(first).isSameInstanceAs(second)
    }

    @Test
    fun `ViewModelScope 인스턴스는 ViewModel 생명주기 동안 유지된다`() {
        val viewModel = TestViewModel()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ViewModelScope(viewModel),
        ) { Any() }

        val first = AndroidContainer.instance(Any::class, Scope.ViewModelScope(viewModel))
        val second = AndroidContainer.instance(Any::class, Scope.ViewModelScope(viewModel))

        assertThat(first).isSameInstanceAs(second)
    }

    @Test
    fun `ViewModelScope 인스턴스는 ViewModel 종료 시 제거된다`() {
        val viewModel = TestViewModel()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ViewModelScope(viewModel),
        ) { Any() }

        val beforeClear = AndroidContainer.instance(Any::class, Scope.ViewModelScope(viewModel))
        viewModel.clear()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ViewModelScope(viewModel),
        ) { Any() }

        val afterClear = AndroidContainer.instance(Any::class, Scope.ViewModelScope(viewModel))

        assertThat(beforeClear).isNotSameInstanceAs(afterClear)
    }

    @Test
    fun `ActivityScope 는 Activity 생명주기 동안 유지된다`() {
        val activity = Robolectric.buildActivity(TestActivity::class.java).create().get()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ActivityScope(activity),
        ) { Any() }

        val first = AndroidContainer.instance(Any::class, Scope.ActivityScope(activity))
        val second = AndroidContainer.instance(Any::class, Scope.ActivityScope(activity))

        assertThat(first).isSameInstanceAs(second)
    }

    @Test
    fun `ActivityScope 는 Activity 가 종료되면 제거된다`() {
        val controller = Robolectric.buildActivity(TestActivity::class.java).create()
        val activity = controller.get()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ActivityScope(activity),
        ) { Any() }

        val beforeDestroy = AndroidContainer.instance(Any::class, Scope.ActivityScope(activity))
        controller.destroy()

        val recreatedActivity = Robolectric.buildActivity(TestActivity::class.java).create().get()

        AndroidContainer.register(
            clazz = Any::class,
            scope = Scope.ActivityScope(recreatedActivity),
        ) { Any() }

        val afterDestroy =
            AndroidContainer.instance(Any::class, Scope.ActivityScope(recreatedActivity))

        assertThat(beforeDestroy).isNotSameInstanceAs(afterDestroy)
    }

    class TestActivity : ComponentActivity()

    class TestViewModel : ViewModel() {
        fun clear() = onCleared()
    }
}
