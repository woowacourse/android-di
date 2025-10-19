package woowacourse.shopping.model.repository

import com.example.di.annotation.ViewModelScoped
import woowacourse.shopping.model.Product

@ViewModelScoped
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
