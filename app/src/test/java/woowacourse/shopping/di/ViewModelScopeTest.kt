package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import woowacourse.di.DIContainer
import woowacourse.shopping.data.ProductRepository

class ViewModelScopeTest {
    private val container =
        DIContainer().apply {
            bind(ProductRepository::class, ProductRepository::class, scopeType = ViewModelScope)
        }
    private val appScopedContainer = container.openScope(AppScope, "test-application")

    @Test
    fun `같은 ViewModel에서는 같은 ProductRepository 인스턴스를 사용한다`() {
        val store = ViewModelStore()
        val factory = DIViewModelFactory(appScopedContainer)

        val viewModel = ViewModelProvider(store, factory)[ProductRepositoryViewModel::class.java]

        assertThat(viewModel.secondRepository).isSameInstanceAs(viewModel.firstRepository)
    }

    @Test
    fun `서로 다른 ViewModel에서는 서로 다른 ProductRepository 인스턴스를 사용한다`() {
        val factory = DIViewModelFactory(appScopedContainer)
        val first = ViewModelProvider(ViewModelStore(), factory)[ProductRepositoryViewModel::class.java]
        val second = ViewModelProvider(ViewModelStore(), factory)[ProductRepositoryViewModel::class.java]

        assertThat(second.firstRepository).isNotSameInstanceAs(first.firstRepository)
    }

    @Test
    fun `ViewModel이 소멸하면 스코프에 저장된 ProductRepository를 제거한다`() {
        val store = ViewModelStore()
        val scopeId = "cleared-view-model"
        val factory = DIViewModelFactory(appScopedContainer) { scopeId }
        val viewModel = ViewModelProvider(store, factory)[ProductRepositoryViewModel::class.java]
        val beforeClear = viewModel.firstRepository

        store.clear()
        val reopenedScope = appScopedContainer.openScope(ViewModelScope, scopeId)
        val afterClear = reopenedScope.get(ProductRepository::class)

        assertThat(afterClear).isNotSameInstanceAs(beforeClear)
    }

    class ProductRepositoryViewModel(
        val firstRepository: ProductRepository,
        val secondRepository: ProductRepository,
    ) : ViewModel()
}
