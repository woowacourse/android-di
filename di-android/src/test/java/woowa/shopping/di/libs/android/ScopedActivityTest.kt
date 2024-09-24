package woowa.shopping.di.libs.android

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import woowa.shopping.di.libs.android.sample.SampleScopedActivity
import woowa.shopping.di.libs.android.sample.SampleRepository
import woowa.shopping.di.libs.android.sample.SampleService
import woowa.shopping.di.libs.android.sample.SampleServiceImpl
import woowa.shopping.di.libs.container.Containers
import woowa.shopping.di.libs.container.startDI

@RunWith(RobolectricTestRunner::class)
class ScopedActivityTest {
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var controller: ActivityController<SampleScopedActivity>
    private val activity: SampleScopedActivity
        get() = controller.get()

    @Before
    fun setUp() {
        startDI {
            container {
                single<SampleService> { SampleServiceImpl() }

                scope<SampleScopedActivity> {
                    scoped { SampleRepository(get()) }
                }
            }
        }
        createActivity()
    }

    @After
    fun tearDown() {
        Containers.clearContainersForTest()
    }

    @Test
    fun `Activity 가 Configuration 되어도 SampleRepository 는 동일 인스턴스를 가진다`() {
        // given
        val sampleRepository = activity.sampleRepository
        // when
        controller.configurationChange()
        val repository = activity.get<SampleRepository>()
        // then
        repository shouldBeSameInstanceAs sampleRepository
    }

    @Test
    fun `Activity 가 Destroy 되면 SampleRepository 는 Scope가 파괴되어 가져올 수 없다`() {
        // when
        controller.destroy()
        // then
        shouldThrow<IllegalArgumentException> {
            activity.get<SampleRepository>()
        }
    }

    @Test
    fun `Activity 가 Destroy 된 후, 다시 생성되면 SampleRepository 는 다시 생성된다`() {
        // onCreate: Scope 생성, SampleRepository 주입
        val repository = activity.sampleRepository
        // onDestroy: Scope 파괴
        controller.destroy()
        // onCreate: Scope 생성, SampleRepository 다시 주입
        controller = buildActivity(SampleScopedActivity::class.java)
        activity.setTheme(com.google.android.material.R.style.Theme_AppCompat)
        controller.setup()
        // then: 새로운 SampleRepository 가 생성된다
        val newRepository = activity.sampleRepository
        // then
        repository shouldNotBe newRepository
    }

    private fun createActivity() {
        controller = buildActivity(SampleScopedActivity::class.java)
        activity.setTheme(com.google.android.material.R.style.Theme_AppCompat)
        controller.setup()
    }
}