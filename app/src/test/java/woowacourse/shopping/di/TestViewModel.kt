package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import woowacourse.shopping.data.ProductRepository

class TestViewModel(
    private val productRepository: ProductRepository,
) : ViewModel()
