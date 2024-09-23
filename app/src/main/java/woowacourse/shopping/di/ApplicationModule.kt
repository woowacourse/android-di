package woowacourse.shopping.di

import org.library.haeum.di.Module
import woowacourse.shopping.data.db.CartProductDao
import woowacourse.shopping.data.db.ShoppingDatabase
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.DBCartRepository
import woowacourse.shopping.data.repository.DefaultCartRepository

class ApplicationModule : Module() {
    @InMemoryRepository
    fun provideInMemoryCartRepository(cartProductDao: CartProductDao): CartRepository = DefaultCartRepository()

    @RoomDBRepository
    fun provideRoomDBCartRepository(cartProductDao: CartProductDao): CartRepository = DBCartRepository(cartProductDao)

    fun provideCartProductDao(): CartProductDao =
        context.let {
            ShoppingDatabase.getDatabase(it ?: throw IllegalArgumentException("Context가 null입니다.")).cartProductDao()
        }
}
