package woowacourse.shopping.di

import android.content.Context
import com.example.bbottodi.di.Module
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.DefaultProductRepository
import woowacourse.shopping.data.repository.InDiskCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.model.repository.ProductRepository
import kotlin.reflect.KClass

object DefaultModule : Module {
    fun provideCartProductDao(context: Context): CartProductDao {
        val localDatabase = ShoppingDatabase.getInstance(context)
        return localDatabase.cartProductDao()
    }

    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    fun provideInDiskCartRepository(): KClass<InDiskCartRepository> {
        return InDiskCartRepository::class
    }

    fun provideInMemoryCartRepository(): KClass<InMemoryCartRepository> {
        return InMemoryCartRepository::class
    }
}
