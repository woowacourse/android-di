package woowacourse.shopping.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.woosuk.scott_di_android.ActivityContext
import com.woosuk.scott_di_android.ActivityScope
import com.woosuk.scott_di_android.Module
import com.woosuk.scott_di_android.Singleton
import com.woosuk.scott_di_android.ViewModelScope
import woowacourse.shopping.ShoppingApplication
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DataBaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

object DefaultModule : Module {
    @Singleton
    fun provideApplication(): Application {
        return ShoppingApplication.instance
    }

    @Singleton
    fun provideAppDatabase(
        application: Application
    ): ShoppingDatabase {
        return Room.databaseBuilder(
            application,
            ShoppingDatabase::class.java,
            "cart_product"
        ).build()
    }

    @Singleton
    fun provideCartProductDao(
        shoppingDatabase: ShoppingDatabase
    ): CartProductDao {
        return shoppingDatabase.cartProductDao()
    }

    @ActivityScope
    fun provideDateFormatter(
        @ActivityContext context: Context
    ): DateFormatter {
        return DateFormatter(context)
    }

    @ViewModelScope
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @Singleton
    @InMemoryCartRepo
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }

    @Singleton
    @DatabaseCartRepo
    fun provideDatabaseCartRepository(
        cartProductDao: CartProductDao
    ): CartRepository {
        return DataBaseCartRepository(cartProductDao = cartProductDao)
    }
}
