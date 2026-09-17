package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import woowacourse.shopping.data.RepositoryContainer

class ShoppingApplication: Application() {
    val repositoryContainer = RepositoryContainer(
        productRepository = ProductRepository(),
        cartRepository = CartRepository()
    )
}