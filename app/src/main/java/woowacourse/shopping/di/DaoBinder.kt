package woowacourse.shopping.di

import com.woowa.di.component.InstallIn
import com.woowa.di.singleton.SingletonComponent
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase

@InstallIn(SingletonComponent::class)
object DaoBinder {
    @Database
    fun provideCartProductDao(
        @Database database: ShoppingDatabase,
    ): CartProductDao = database.cartProductDao()
}
