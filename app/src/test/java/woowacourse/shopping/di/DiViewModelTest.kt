package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.ui.cart.CartViewModel
import woowacourse.shopping.ui.products.ProductsViewModel
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


class DiViewModel : ViewModel() {
    fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 전달받은 뷰모델의 타입을 런타임시점에 생성한다.
        val constructor = modelClass.kotlin.primaryConstructor!!

        // 해당 뷰모델의 생성자의 파라미터의 클래스 타입을 알아낸다.
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }

        val typesConstructors = types.map { type ->
            type.primaryConstructor!!.call()
        }

        return constructor.call(*typesConstructors.toTypedArray())
    }
}

@RunWith(RobolectricTestRunner::class)
class DiViewModelTest {
    @Test
    fun `ViewModel을 받았을 때 해당 ViewModel이 어떤 클래스인지 알 수 있다`() {
        // given
        val diViewModel = DiViewModel()

        // when
        val productsViewModel = diViewModel.create(ProductsViewModel::class.java)
        val cartViewModel = diViewModel.create(CartViewModel::class.java)

        // then
        assertThat(productsViewModel::class.simpleName).isEqualTo("ProductsViewModel")
        assertThat(cartViewModel::class.simpleName).isEqualTo("CartViewModel")
    }
}
