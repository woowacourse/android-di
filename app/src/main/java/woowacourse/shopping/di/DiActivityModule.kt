package woowacourse.shopping.di

import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository

class DiActivityModule(parentDiContainer: DiContainer) : DiContainer(parentDiContainer) {
    val provideCartInDiskRepository: CartRepository by lazy {
        this.createInstance(CartInDiskRepository::class)
    }

    val provideCartProductDao: CartProductDao by lazy {
        this.get(ShoppingDatabase::class)!!.cartProductDao()
    }
}
