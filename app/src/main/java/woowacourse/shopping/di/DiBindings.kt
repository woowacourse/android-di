package woowacourse.shopping.di

import android.content.Context
import woowacourse.bibi.di.core.ActivityScope
import woowacourse.bibi.di.core.AppScope
import woowacourse.bibi.di.core.ContainerBuilder
import woowacourse.bibi.di.core.Local
import woowacourse.bibi.di.core.ViewModelScope
import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

fun installAllBindings(
    builder: ContainerBuilder,
    appContext: Context,
) {
    val db = ShoppingDatabase.getInstance(appContext)
    val cartDao = db.cartProductDao()

    builder.register(
        ProductRepository::class,
        Local::class,
        ViewModelScope::class,
    ) { ProductRepositoryImpl() }
    builder.register(CartRepository::class, Local::class, AppScope::class) {
        CartRepositoryImpl(
            cartDao,
        )
    }

    builder.register(Context::class, AppScope::class) { appContext.applicationContext }
    builder.register(
        DateFormatter::class,
        ActivityScope::class,
    ) { DateFormatter(appContext.applicationContext) }
}
