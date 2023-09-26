package woowacourse.shopping.di

import android.content.Context
import com.example.bbottodi.di.Module
import com.example.bbottodi.di.annotation.InDisk
import com.example.bbottodi.di.annotation.InMemory
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.InDiskCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.model.repository.CartRepository

class ApplicationModule(private val context: Context) : Module {
    fun provideContext() = context

    fun provideCartProductDao(context: Context): CartProductDao {
        val localDatabase = ShoppingDatabase.getInstance(context)
        return localDatabase.cartProductDao()
    }

    @InDisk
    fun provideInDiskCartRepository(cartProductDao: CartProductDao): CartRepository {
        return InDiskCartRepository(cartProductDao)
    }

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }
}
