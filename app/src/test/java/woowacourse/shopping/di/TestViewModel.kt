package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.ProductRepository

class TestViewModel(
    private val productRepository: ProductRepository,
) : ViewModel()

class FieldInjectionTestViewModel : ViewModel() {
    @Inject
    lateinit var injectedRepository: ProductRepository

    lateinit var ignoredRepository: ProductRepository

    fun isInjectedRepositoryInitialized(): Boolean = ::injectedRepository.isInitialized

    fun isIgnoredRepositoryInitialized(): Boolean = ::ignoredRepository.isInitialized
}
