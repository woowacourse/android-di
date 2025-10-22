package woowacourse.di

import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import woowacourse.shopping.android.di.AndroidContainer
import woowacourse.shopping.android.di.Scope
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.model.ProductRepository

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeTest {
    @Test
    fun `ProductRepository는 ViewModel LifeCycle 동안 유지된다`() {
        // given
        val controller =
            Robolectric
                .buildActivity(TestActivity::class.java)
                .create()

        val beforeRecreate = controller.get().viewModel.productRepository

        controller.recreate()

        val afterRecreate = controller.get().viewModel.productRepository

        // then
        assertThat(beforeRecreate).isSameInstanceAs(afterRecreate)
    }

    private class TestActivity : ComponentActivity() {
        val viewModel: ViewModelScopeTestViewModel by viewModels()
    }

    class ViewModelScopeTestViewModel : ViewModel() {
        init {
            AndroidContainer.register(
                ProductRepository::class,
                Scope.ViewModelScope(this),
            ) { DefaultProductRepository() }
        }

        val productRepository: ProductRepository =
            AndroidContainer.instance(ProductRepository::class, Scope.ViewModelScope(this))
    }
}
