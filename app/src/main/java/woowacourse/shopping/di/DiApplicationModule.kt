package woowacourse.shopping.di

import android.content.Context
import com.re4rk.arkdi.DiContainer
import com.re4rk.arkdi.Singleton
import com.re4rk.arkdi.annotations.ContextType
import com.re4rk.arkdi.annotations.ContextType.Type.APPLICATION
import com.re4rk.arkdi.annotations.StorageType
import com.re4rk.arkdi.annotations.StorageType.Type.DATABASE
import com.re4rk.arkdi.annotations.StorageType.Type.IN_MEMORY
import woowacourse.shopping.data.CartInDiskRepository
import woowacourse.shopping.data.CartInMemoryRepository
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository

@Suppress("unused")
class DiApplicationModule(
    private val applicationContext: Context,
) : DiContainer() {
    @Singleton
    @ContextType(APPLICATION)
    fun provideApplicationContext(): Context = applicationContext

    @Singleton
    @StorageType(IN_MEMORY)
    fun provideCartRepositoryInMemory(): CartRepository = CartInMemoryRepository()

    @Singleton
    fun provideShoppingDatabase(
        @ContextType(APPLICATION) context: Context,
    ): ShoppingDatabase = ShoppingDatabase.getInstance(context)

    @Singleton
    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase,
    ): CartProductDao = shoppingDatabase.cartProductDao()

    @Singleton
    @StorageType(DATABASE)
    fun provideCartInDiskRepository(
        cartProductDao: CartProductDao,
    ): CartRepository = CartInDiskRepository(cartProductDao)
}
