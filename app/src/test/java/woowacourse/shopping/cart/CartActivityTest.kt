package woowacourse.shopping.cart

import androidx.lifecycle.ViewModelProvider
import org.junit.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.fake.FakeCartProductDao
import woowacourse.fake.FakeDatabaseModule
import woowacourse.fake.FakeRepositoryModule
import woowacourse.peto.di.DependencyContainer
import woowacourse.peto.di.ViewModelFactoryInjector
import woowacourse.shopping.ui.cart.CartActivity
import woowacourse.shopping.ui.cart.DateFormatterModule
import woowacourse.shopping.ui.cart.vm.CartViewModel

@RunWith(RobolectricTestRunner::class)
class CartActivityTest {
    @Test
    fun `CartViewModel 주입 테스트`() {
        // given
        val activity =
            Robolectric.buildActivity(CartActivity::class.java)
                .create()
                .get()

        val dependencyContainer =
            DependencyContainer(
                listOf(
                    DateFormatterModule(activity),
                    FakeDatabaseModule(),
                    FakeRepositoryModule(FakeCartProductDao()),
                ),
            )
        val factory = ViewModelFactoryInjector(dependencyContainer)

        // when
        val viewModel = ViewModelProvider(activity, factory)[CartViewModel::class.java]

        // then
        assertNotNull(viewModel)
        assertNotNull(activity.dateFormatter)
    }
}
