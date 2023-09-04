package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.di.AutoInjector
import woowacourse.shopping.di.activity.DiEntryPointActivity
import woowacourse.shopping.di.application.DiApplication
import woowacourse.shopping.di.module.ApplicationModule
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel
import woowacourse.util.FakeApplicationModule
import woowacourse.util.getFakeActivityModule
import woowacourse.util.getFakeApplicationModule
import woowacourse.util.getProducts

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        // 애플리케이션의 인젝터 객체 페이크객체로 교체. 이제 자동주입 로직이 해당 인젝터가 가진 모듈객체를 따를 것임.
        val application =
            ApplicationProvider.getApplicationContext<DiApplication<ApplicationModule>>()

        val applicationModule =
            DiApplication::class.java.getDeclaredField("applicationModule")
        applicationModule.apply {
            isAccessible = true
            set(application, getFakeApplicationModule())
        }
    }

    @Test
    fun `FakeApplicationModule 초기화 실행 테스트`() {
        // given
        val application =
            ApplicationProvider.getApplicationContext<DiApplication<FakeApplicationModule>>()

        // then
        assertThat(application is DiApplication<FakeApplicationModule>).isEqualTo(true)
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activityController = Robolectric.buildActivity(MainActivity::class.java)
        val activity = activityController.get()
        changeFakeActivityModule(activity)

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activityController = Robolectric.buildActivity(MainActivity::class.java)
        val activity = activityController.get()
        changeFakeActivityModule(activity)
        activityController.create()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `MainViewModel 상품 목록 조회 테스트`() {
        // given
        val activityController = Robolectric.buildActivity(MainActivity::class.java)
        val activity = activityController.get()
        changeFakeActivityModule(activity)
        activityController.create()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // when
        viewModel.getAllProducts()

        // then
        val actual = getProducts()
        assertThat(viewModel.products.value).isEqualTo(actual)
    }

    private fun changeFakeActivityModule(
        activity: DiEntryPointActivity<*>,
    ) {
        DiApplication.removeInjectorForInstance(activity.hashCode())
        DiApplication.addInjectorForInstance(
            activity.hashCode(),
            AutoInjector(getFakeActivityModule(DiApplication.applicationModule)),
        )
    }
}
