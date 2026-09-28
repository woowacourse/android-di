package woowacourse.shopping

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.repository_impl.DefaultCartRepository
import woowacourse.shopping.model.Product
import woowacourse.shopping.ui.products.ProductsViewModel
import woowacourse.shopping.util.annotations.InjectField
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
class SamDiTest {
    @Test
    fun `애노테이션이 붙은 ViewModel 필드만 주입한다`() {
        val application = RuntimeEnvironment.getApplication() as MyApplication
        val factory = SamDi.viewModelFactory(application)

        val productsViewModel = factory.create(ProductsViewModel::class.java)
        val fieldViewModel = factory.create(FieldInjectionViewModel::class.java)

        assertThat(productsViewModel.productRepository)
            .isSameInstanceAs(application.appContainer.productRepository)
        assertThat(fieldViewModel.injected)
            .isSameInstanceAs(application.appContainer.productRepository)
        assertThat(fieldViewModel.unannotated)
            .isSameInstanceAs(FieldInjectionViewModel.originalRepository)
    }

    @Test
    fun `CartRepository의 DAO 의존성을 재귀적으로 해결한다`() = runTest {
        val application = RuntimeEnvironment.getApplication() as MyApplication
        val repository = SamDi.resolve(CartRepository::class, application) as CartRepository
        val product = Product(name = "DI-${UUID.randomUUID()}", price = 1, imageUrl = "")

        assertThat(repository).isInstanceOf(DefaultCartRepository::class.java)

        repository.addCartProduct(product)
        val inserted = repository.getAllCartProducts().single { it.product.name == product.name }
        assertThat(inserted.product.price).isEqualTo(product.price)

        repository.deleteCartProduct(inserted.id)
        assertThat(repository.getAllCartProducts().none { it.id == inserted.id }).isTrue()
    }
}

class FieldInjectionViewModel : ViewModel() {
    companion object {
        val originalRepository = ProductRepository()
    }

    @InjectField
    lateinit var injected: ProductRepository

    var unannotated: ProductRepository = originalRepository
}
