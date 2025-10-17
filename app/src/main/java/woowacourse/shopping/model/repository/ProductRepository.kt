package woowacourse.shopping.model.repository

import com.example.di_v2.annotation.ViewModelScoped
import woowacourse.shopping.model.Product

@ViewModelScoped
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
