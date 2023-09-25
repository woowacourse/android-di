package woowacourse.shopping.di.module

import android.content.Context
import androidx.room.Room
import woowacourse.shopping.annotation.ApplicationContext
import woowacourse.shopping.annotation.Binds
import woowacourse.shopping.annotation.Provides
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.annotation.UnDisposableCartRepositoryQualifier
import woowacourse.shopping.module.DIApplicationModule

class ApplicationModule : DIApplicationModule() {
    @Binds
    @UnDisposableCartRepositoryQualifier
    private lateinit var bindCartRepository: DatabaseCartRepository

    @Provides
    private fun provideCartProductDao(@ApplicationContext context: Context): CartProductDao {
        return Room.databaseBuilder(
            context,
            ShoppingDatabase::class.java,
            ShoppingDatabase.name,
        ).build().cartProductDao()
    }
}
