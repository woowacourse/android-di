package woowacourse.shopping.application

import android.content.Context
import com.example.di.Module
import com.example.di.annotation.Database
import com.example.di.annotation.InMemory
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.db.ShoppingDatabase

class AppModule(private val context: Context) : Module {
    fun provideCartProductDao(): CartProductDao {
        return ShoppingDatabase.getInstance(context).cartProductDao()
    }

    @Database
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository {
        return DatabaseCartRepository(cartProductDao)
    }

    @InMemory
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }

    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }
}
