package woowacourse.shopping.di

import android.content.Context
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.Remote
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository

fun installAllBindings(
    builder: ContainerBuilder,
    appContext: Context,
) {
    val db = ShoppingDatabase.getInstance(appContext)
    val cartDao = db.cartProductDao()

    builder.register(ProductRepository::class, Remote::class) { ProductRepositoryImpl() }
    builder.register(CartRepository::class, Remote::class) { CartRepositoryImpl(cartDao) }
}
