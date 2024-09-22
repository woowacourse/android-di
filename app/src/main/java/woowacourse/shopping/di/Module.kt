package woowacourse.shopping.di

import android.content.Context
import org.library.haeum.HaeumContext
import org.library.haeum.Module
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.DBCartRepository
import woowacourse.shopping.data.repository.DefaultCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.ProductRepository

@Module
object Module {
    fun provideShoppingDatabase(
        @HaeumContext context: Context,
    ): ShoppingDatabase = ShoppingDatabase.getDatabase(context)

    fun provideProductRepository(): ProductRepository = DefaultProductRepository

    @RoomDBRepository
    fun provideRoomDBCartRepository(shoppingDatabase: ShoppingDatabase): CartRepository =
        DBCartRepository(shoppingDatabase.cartProductDao())

    @InMemoryRepository
    fun provideInMemoryCartRepository(): CartRepository = DefaultCartRepository()
}
