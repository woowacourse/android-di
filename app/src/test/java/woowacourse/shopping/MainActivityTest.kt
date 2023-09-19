package woowacourse.shopping

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.test.core.app.launchActivity
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.MainActivity
import woowacourse.shopping.ui.MainViewModel

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `Activity 실행 테스트`() {
        // given
        val activity = Robolectric
            .buildActivity(MainActivity::class.java)
            .create()
            .get()

        // then
        assertNotNull(activity)
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
        assertNotNull(viewModel)
    }


    @Test
    fun `Activity를 종료하고 다시 생성했을 때, 싱글톤은 동일한 인스턴스가 주입되고 싱글톤이 아니면 새로운 인스턴스가 주입된다`() {
        // GIVEN
        var scenario = launchActivity<MainActivity>()

        var firstProductRepository: ProductRepository? = null
        var firstCartRepository: CartRepository? = null
        var secondProductRepository: ProductRepository? = null
        var secondCartRepository: CartRepository? = null

        // WHEN
        scenario.moveToState(Lifecycle.State.CREATED)

        // THEN
        scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

            firstProductRepository = viewModel::class.java.getDeclaredField("productRepository")
                .apply { this.isAccessible = true }.get(viewModel) as ProductRepository
            assertNotNull(firstProductRepository)

            firstCartRepository = viewModel::class.java.getDeclaredField("cartRepository")
                .apply { this.isAccessible = true }.get(viewModel) as CartRepository
            assertNotNull(firstCartRepository)
        }

        // WHEN : 액티비티 destroy 후 다시 생성
        scenario.moveToState(Lifecycle.State.DESTROYED)
        scenario = launchActivity<MainActivity>()

        // THEN
        scenario.onActivity { activity ->
            val viewModel = ViewModelProvider(activity)[MainViewModel::class.java]

            secondProductRepository = viewModel::class.java.getDeclaredField("productRepository")
                .apply { this.isAccessible = true }.get(viewModel) as ProductRepository
            assertNotNull(secondProductRepository)

            secondCartRepository = viewModel::class.java.getDeclaredField("cartRepository")
                .apply { this.isAccessible = true }.get(viewModel) as CartRepository
            assertNotNull(secondCartRepository)
        }

        // instance 비교
        assert(firstCartRepository === secondCartRepository)
        assert(firstProductRepository !== secondProductRepository)
    }
}
