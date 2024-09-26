package woowa.shopping.di.libs.android

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import woowa.shopping.di.libs.android.sample.SampleActivity
import woowa.shopping.di.libs.android.sample.SampleRepository
import woowa.shopping.di.libs.android.sample.SampleService
import woowa.shopping.di.libs.android.sample.SampleServiceImpl
import woowa.shopping.di.libs.android.sample.SampleViewModel
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI
import woowa.shopping.di.libs.inject.injectScopeComponent
import woowa.shopping.di.libs.utils.addLogger

@RunWith(AndroidJUnit4::class)
class ScopedViewModelTest {
    @Before
    fun setUp() {
        startDI {
            container {
                single<SampleService> { SampleServiceImpl() }
                viewModel<SampleViewModel>(
                    viewModelFactory = { SampleViewModel(get()) },
                    configureBindings = {
                        scoped { SampleRepository(get()) }
                    },
                )
            }
        }
    }

    @After
    fun tearDown() {
        Containers.clearContainersForTest()
    }

    @Test
    fun `Activity 에서 ViewModel 주입 테스트`() {
        launchActivity<SampleActivity>().onActivity { activity ->
            activity.viewModel shouldBeSameInstanceAs activity.getViewModel<SampleViewModel>()
        }
    }

    @Test
    fun `ViewModel 은 Activity 가 파괴되면 같이 파괴된다`() {
        // given
        val scenario = launchActivity<SampleActivity>().addLogger()
        val viewModel by injectViewModel<SampleViewModel>()
        // when
        scenario.onActivity { activity ->
            scenario.moveToState(Lifecycle.State.DESTROYED)
            // then
            shouldThrow<IllegalStateException> {
                viewModel
            }
        }
    }

    @Test
    fun `ViewModel 은 Configuration 변경 시 파괴되지 않는다`() {
        // given
        val scenario = launchActivity<SampleActivity>().addLogger()
        val viewModel by injectViewModel<SampleViewModel>()
        // when
        scenario.recreate()
        // then
        scenario.onActivity { configureChangedActivity ->
            configureChangedActivity.getViewModel<SampleViewModel>() shouldBeSameInstanceAs viewModel
        }
    }

    @Test
    fun `SampleRepository 은 Configuration 변경 시 파괴되지 않는다`() {
        // given
        val scenario = launchActivity<SampleActivity>().addLogger()
        val repository by injectScopeComponent<SampleRepository>(scopeClazz = SampleViewModel::class)
        // when
        scenario.recreate()
        // then
        scenario.onActivity { configureChangedActivity ->
            shouldNotThrow<IllegalStateException> {
                repository
            }
            shouldNotThrow<IllegalArgumentException> {
                repository
            }
        }
    }

    @Test
    fun `SampleRepository 은 Activity 파괴시 Lock 이 걸린다`() {
        // given
        val scenario = launchActivity<SampleActivity>().addLogger()
        val repository by injectScopeComponent<SampleRepository>(scopeClazz = SampleViewModel::class)
        scenario.onActivity { configureChangedActivity ->
            // when
            scenario.moveToState(Lifecycle.State.DESTROYED)
            // then
            shouldThrow<IllegalStateException> {
                repository
            }
        }
    }
}
