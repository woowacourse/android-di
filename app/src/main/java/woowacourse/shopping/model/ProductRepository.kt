package woowacourse.shopping.model

import com.woowacourse.di.ViewModelScope

@ViewModelScope
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
