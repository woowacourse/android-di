package woowacourse.shopping.android.di

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.core.di.Inject

@RunWith(RobolectricTestRunner::class)
class ViewModelStoreOwnerTest {
    @Test
    fun `ViewModel 생성자에 의존성이 불필요한 경우 - 성공`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: NoDependencyViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        // then
        assertThat(activity.viewModel).isNotNull
    }

    @Test
    fun `ViewModel 생성자에 선언된 의존성이 필요한 경우 - 성공`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: DeclaredDependenciesViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        AndroidContainer
            .ofActivity(activity)
            .register(DeclaredDependency::class) { DefaultDeclaredDependency() }

        // then
        assertThat(activity.viewModel.declaredDependency).isNotNull
    }

    @Test
    fun `ViewModel 생성자에 선언되지 않은 의존성이 필요한 경우 - 실패`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: UnknownDependenciesViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        // then
        assertThrows(IllegalStateException::class.java) { activity.viewModel }
    }

    @Test
    fun `ViewModel 생성자에 선언된 의존성이 필요하며, 파라미터 기본값이 존재하는 경우 - 선언된 의존성 사용`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: DeclaredDependenciesWithDefaultParameterViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        AndroidContainer
            .ofActivity(activity)
            .register(DeclaredDependency::class) { DefaultDeclaredDependency() }

        // then
        assertThat(activity.viewModel.declaredDependency).isInstanceOf(DefaultDeclaredDependency::class.java)
    }

    @Test
    fun `@Inject 어노테이션이 붙은 프로퍼티에 의존성이 필요한 경우 - 주입 성공`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: PropertyInjectViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        AndroidContainer
            .ofActivity(activity)
            .register(DeclaredDependency::class) { DefaultDeclaredDependency() }

        // then
        assertThat(activity.viewModel.declaredDependency).isInstanceOf(DefaultDeclaredDependency::class.java)
    }

    @Test
    fun `@Inject 어노테이션이 붙지 않은 프로퍼티에 의존성이 필요한 경우 - 주입 실패`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: PropertyWithoutInjectViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        // then
        assertThrows(UninitializedPropertyAccessException::class.java) { activity.viewModel.declaredDependency }
    }

    @Test
    fun `@Inject 어노테이션이 붙은 프로퍼티에 선언되지 않은 의존성이 필요한 경우 - 주입 실패`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: UnknownPropertyInjectViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        // then
        assertThrows(IllegalStateException::class.java) { activity.viewModel }
    }

    @Test
    fun `@Inject 어노테이션이 붙은 프로퍼티가 기본값을 가지는 경우 - AppContainer 의존성으로 덮어씀`() {
        // given
        class TestActivity : ComponentActivity() {
            val viewModel: PropertyInjectWithDefaultValueViewModel by viewModel()
        }

        val activity =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()
                .get()

        AndroidContainer
            .ofActivity(activity)
            .register(DeclaredDependency::class) { DefaultDeclaredDependency() }

        // then
        assertThat(activity.viewModel.declaredDependency).isInstanceOf(DefaultDeclaredDependency::class.java)
    }
}

interface DeclaredDependency

class DefaultDeclaredDependency : DeclaredDependency

class DefaultParameterDependency : DeclaredDependency

interface UnknownDependency

class NoDependencyViewModel : ViewModel()

class DeclaredDependenciesViewModel(
    val declaredDependency: DeclaredDependency,
) : ViewModel()

class UnknownDependenciesViewModel(
    unknownDependency: UnknownDependency,
) : ViewModel()

class DeclaredDependenciesWithDefaultParameterViewModel(
    val declaredDependency: DeclaredDependency = DefaultParameterDependency(),
) : ViewModel()

class PropertyInjectViewModel : ViewModel() {
    @Inject
    lateinit var declaredDependency: DeclaredDependency
}

class PropertyWithoutInjectViewModel : ViewModel() {
    lateinit var declaredDependency: DeclaredDependency
}

class UnknownPropertyInjectViewModel : ViewModel() {
    @Inject
    lateinit var declaredDependency: UnknownDependency
}

class PropertyInjectWithDefaultValueViewModel : ViewModel() {
    @Inject
    var declaredDependency: DeclaredDependency = object : DeclaredDependency {}
}
