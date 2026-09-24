package woowacourse.shopping

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.common.truth.Truth.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.di.AutoViewModelFactory
import woowacourse.shopping.di.DependencyContainer
import woowacourse.shopping.model.CartProduct
import woowacourse.shopping.model.Product

@RunWith(RobolectricTestRunner::class)
class AutoViewModelFactoryTest {
    @Test
    fun `ViewModelProvider가 생성자 의존성을 주입한다`() {
        val repository = FakeCartRepository()
        val container =
            DependencyContainer().apply {
                registerInstance(FakeCartRepository::class, repository)
            }
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
        val provider = ViewModelProvider(activity, AutoViewModelFactory(container))

        val viewModel = provider[CartRepositoryTestViewModel::class.java]

        assertThat(viewModel.repository).isSameInstanceAs(repository)
    }

    @Test
    fun `ViewModelProvider는 의존성이 없으면 생성에 실패한다`() {
        val container = DependencyContainer()
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
        val provider = ViewModelProvider(activity, AutoViewModelFactory(container))

        assertThatThrownBy { provider[MissingDependencyTestViewModel::class.java] }
            .isInstanceOf(IllegalStateException::class.java)
    }
}

class CartRepositoryTestViewModel(
    val repository: FakeCartRepository,
) : ViewModel()

class MissingDependencyTestViewModel(
    val repository: UnregisteredDependency,
) : ViewModel()

class FakeCartRepository : CartRepository {
    override suspend fun addCartProduct(product: Product) = Unit

    override suspend fun getAllCartProducts(): List<CartProduct> = emptyList()

    override suspend fun deleteCartProduct(id: Long) = Unit
}
