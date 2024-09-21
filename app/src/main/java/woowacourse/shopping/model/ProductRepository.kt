package woowacourse.shopping.model

import com.example.di.annotation.Singleton

@Singleton
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
