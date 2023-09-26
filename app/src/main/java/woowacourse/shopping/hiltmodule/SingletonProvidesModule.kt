package woowacourse.shopping.hiltmodule

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import woowacourse.shopping.data.CartDatabase
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.di.annotation.Database
import woowacourse.shopping.di.annotation.InMemory
import woowacourse.shopping.repository.CartRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonProvidesModule {
    @Provides
    @Singleton
    fun provideCartDao(@ApplicationContext context: Context): CartProductDao {
        val database = Room
            .databaseBuilder(context, CartDatabase::class.java, "krrong-database")
            .build()
        return database.cartProductDao()
    }

    @Provides
    @Singleton
    @Database
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository {
        return DatabaseCartRepository(cartProductDao)
    }

    @Provides
    @Singleton
    @InMemory
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }
}
