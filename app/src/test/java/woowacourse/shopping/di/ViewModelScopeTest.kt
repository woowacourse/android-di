package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import woowacourse.di.Injector
import woowacourse.di.get
import woowacourse.di.injector
import woowacourse.di.scoped
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.ui.products.ProductsViewModel

@RunWith(RobolectricTestRunner::class)
class ViewModelScopeTest {
    @Test
    fun `ViewModel마다 상품 저장소가 다르고 앱 장바구니는 공유한다`() {
        val application = ApplicationProvider.getApplicationContext<ShoppingApplication>()
        val root = application.injector
        val store = ViewModelStore()
        val extras =
            MutableCreationExtras().apply {
                set(ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY, application)
            }
        val provider = ViewModelProvider(store, ViewModelFactory, extras)
        try {
            val first = provider["first", ProductsViewModel::class.java]
            val second = provider["second", ProductsViewModel::class.java]
            val scope = requireNotNull(first.getCloseable<Injector>(VIEW_MODEL_SCOPE_KEY))
            assertThat(provider["first", ProductsViewModel::class.java]).isSameAs(first)
            assertThat(scope.get<ProductRepository>()).isSameAs(first.productRepository)
            assertThat(first.productRepository).isNotSameAs(second.productRepository)
            assertThat(first.cartRepository).isSameAs(second.cartRepository)

            store.clear()

            assertThat(scope.isClosed).isTrue()
            assertThat(requireNotNull(second.getCloseable<Injector>(VIEW_MODEL_SCOPE_KEY)).isClosed).isTrue()
            assertThat(root.get<CartRepository>(RoomCart::class)).isSameAs(first.cartRepository)
            val recreated = provider["first", ProductsViewModel::class.java]
            assertThat(recreated.productRepository).isNotSameAs(first.productRepository)
            assertThat(recreated.cartRepository).isSameAs(first.cartRepository)
        } finally {
            store.clear()
            root.close()
        }
    }

    @Test
    fun `ViewModelStore를 반복 정리하면 모든 상품 저장소의 종료 콜백이 실행된다`() {
        var created = 0
        var released = 0
        val root =
            injector {
                scoped<ProductRepository>(ShoppingScopes.ViewModel, onClose = { released++ }) {
                    created++
                    ProductRepository()
                }
            }
        val factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras,
                ): T = root.createViewModel(modelClass)
            }
        try {
            repeat(20) {
                val store = ViewModelStore()
                val model = ViewModelProvider(store, factory)[RepositoryViewModel::class.java]
                val scope = requireNotNull(model.getCloseable<Injector>(VIEW_MODEL_SCOPE_KEY))
                assertThat(scope.get<ProductRepository>()).isSameAs(model.repository)
                store.clear()
                assertThat(scope.isClosed).isTrue()
                assertThat(released).isEqualTo(created)
            }
            assertThat(created).isEqualTo(20)
        } finally {
            root.close()
        }
        assertThat(released).isEqualTo(20)
    }

    @Test
    fun `ViewModel 생성이 실패해도 먼저 만든 스코프 의존성을 해제한다`() {
        var released = 0
        val root =
            injector {
                scoped<ProductRepository>(ShoppingScopes.ViewModel, onClose = { released++ })
            }
        try {
            assertThatThrownBy { root.createViewModel(FailingViewModel::class.java) }
                .hasMessageContaining("MissingDependency")
            assertThat(released).isEqualTo(1)
        } finally {
            root.close()
        }
        assertThat(released).isEqualTo(1)
    }

    class RepositoryViewModel(
        val repository: ProductRepository,
    ) : ViewModel()

    class FailingViewModel(
        val repository: ProductRepository,
        val missing: MissingDependency,
    ) : ViewModel()

    interface MissingDependency
}
