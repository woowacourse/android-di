package woowacourse.shopping.di.module

import android.content.Context
import com.boogiwoogi.di.DefaultModule
import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.Qualifier
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository

class ShoppingApplicationModule(private val context: Context) : DefaultModule() {

    @Qualifier("ApplicationContext")
    @Provides
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Qualifier("DatabaseCartRepository")
    @Provides
    fun provideDatabaseCartRepository(): CartRepository {
        return DatabaseCartRepository(
            ShoppingDatabase.getDatabase(context).cartProductDao()
        )
    }

    @Qualifier("InMemoryCartRepository")
    @Provides
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }

    companion object {
        private const val CONTEXT_TYPE_ERROR = "inappropriate context"
    }
}
