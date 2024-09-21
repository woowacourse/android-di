package woowacourse.shopping.model

import com.example.di.Singleton

@Singleton
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
