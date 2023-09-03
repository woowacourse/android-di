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
import woowacourse.shopping.di.Injector
import woowacourse.shopping.global.ShoppingApplication
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel
import woowacourse.util.getFakeNormalModule
import woowacourse.util.getFakeSingletonModule
import woowacourse.util.getProducts

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var fakeInjector: Injector

    @Before
    fun setup() {
        fakeInjector = AutoInjector(listOf(getFakeSingletonModule(), getFakeNormalModule()))

        // 애플리케이션의 인젝터 객체 페이크객체로 교체. 이제 자동주입 로직이 해당 인젝터가 가진 모듈객체를 따를 것임.
        val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
        val injector = ShoppingApplication::class.java.getDeclaredField("injector")
        injector.apply {
            isAccessible = true
            set(application, fakeInjector)
        }
    }

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertThat(activity).isNotNull()
    }

    @Test
    fun `ViewModel 주입 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        // then
        assertThat(viewModel).isNotNull()
    }

    @Test
    fun `MainViewModel 상품 목록 조회 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()
        val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

        viewModel.getAllProducts()

        // then
        val actual = getProducts()
        assertThat(viewModel.products.value).isEqualTo(actual)
    }
}
