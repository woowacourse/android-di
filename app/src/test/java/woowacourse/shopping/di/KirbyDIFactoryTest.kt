package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel

class KirbyDIFactoryTest {
    class AdditionalViewModel(
        val cartRepository: CartRepository,
    ) : ViewModel()

    class DependencyHolder(
        val cartRepository: CartRepository,
    )

    class NestedViewModel(
        val holder: DependencyHolder,
    ) : ViewModel()

    @Test
    fun `두 화면의 ViewModel을 같은 Factory로 생성한다`() {
        assertThat(KirbyDIFactory.create(ProductsViewModel::class.java)).isNotNull()
        assertThat(KirbyDIFactory.create(CartViewModel::class.java)).isNotNull()
    }

    @Test
    fun `새 ViewModel도 주입 코드 추가 없이 생성하며 Repository를 공유한다`() {
        val first = KirbyDIFactory.create(AdditionalViewModel::class.java)
        val second = KirbyDIFactory.create(AdditionalViewModel::class.java)
        val nested = KirbyDIFactory.create(NestedViewModel::class.java)

        assertThat(first).isNotSameInstanceAs(second)
        assertThat(first.cartRepository).isSameInstanceAs(second.cartRepository)
        assertThat(first.cartRepository).isSameInstanceAs(nested.holder.cartRepository)
    }
}
