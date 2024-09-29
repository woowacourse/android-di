package woowacourse.shopping.di

import android.content.Context
import com.example.seogi.di.Module
import com.example.seogi.di.annotation.ActivityScoped
import com.example.seogi.di.annotation.Provides
import com.example.seogi.di.annotation.Qualifier
import com.example.seogi.di.annotation.SingleTone
import com.example.seogi.di.annotation.ViewModelScoped
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.CartRepositoryInMemory
import woowacourse.shopping.data.CartRepositoryOnDisk
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.repository.CartRepository
import woowacourse.shopping.model.repository.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter

@Qualifier
annotation class OnDisk

@Qualifier
annotation class InMemory

object DiModule : Module {
    @ViewModelScoped
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl()

    @InMemory
    @SingleTone
    fun provideCartRepositoryInMemory(): CartRepository = CartRepositoryInMemory()

    @OnDisk
    @SingleTone
    fun provideCartRepositoryOnDisk(): CartRepository = CartRepositoryOnDisk()

    @ActivityScoped
    fun provideDateFormatter(context: Context): DateFormatter = DateFormatter(context)

    @Provides
    @SingleTone
    fun provideAppDatabase(context: Context): ShoppingDatabase = ShoppingDatabase.getInstance(context)

    @Provides
    @SingleTone
    fun provideCartProductDao(appDatabase: ShoppingDatabase): CartProductDao = appDatabase.cartProductDao()
}
