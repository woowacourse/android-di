package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.ShoppingDatabase

class DefaultAppModule(
    appContext: Context,
) : AppModule {
    val database = ShoppingDatabase.getInstance(appContext)
}
