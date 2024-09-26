package woowacourse.shopping.domain

import com.zzang.di.annotation.lifecycle.ViewModelComponent
import woowacourse.shopping.model.Product

@ViewModelComponent
interface ProductRepository {
    fun getAllProducts(): List<Product>
}
