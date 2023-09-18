package woowacourse.shopping.di.module

import android.content.Context
import com.ssu.di.annotation.Qualifier
import com.ssu.di.module.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotations.StorageType
import woowacourse.shopping.repository.CartRepository

class ApplicationModule(private val context: Context) : Module {

    fun provideCartProductDao(): CartProductDao {
        val db = ShoppingDatabase.getInstance(context.applicationContext)
        return db.cartProductDao()
    }

    @Qualifier(StorageType.DATABASE)
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository =
        DefaultCartRepository(cartProductDao)

    @Qualifier(StorageType.IN_MEMORY)
    fun provideInMemoryCartRepository(): CartRepository = InMemoryCartRepository()
}
