package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

class AppContainer(
    context: Context,
) {
    val cartProductDao: CartProductDao by lazy {
        ShoppingDatabase
            .getDatabase(
                context,
            ).cartProductDao()
    }
}
