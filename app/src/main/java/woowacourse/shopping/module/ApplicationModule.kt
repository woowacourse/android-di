package woowacourse.shopping.module

import android.content.Context
import com.example.di.DIModule
import com.example.di.annotation.LifeCycle
import com.example.di.annotation.LifeCycleScope
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.db.ShoppingDatabase

class ApplicationModule(private val context: Context) : DIModule {
    @LifeCycle(LifeCycleScope.APPLICATION)
    @Qualifier(QualifierType.ApplicationContext)
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @LifeCycle(LifeCycleScope.APPLICATION)
    fun provideCartProductDao(): CartProductDao {
        return ShoppingDatabase.getInstance(context).cartProductDao()
    }

    @LifeCycle(LifeCycleScope.APPLICATION)
    @Qualifier(QualifierType.InMemory)
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }

    @LifeCycle(LifeCycleScope.APPLICATION)
    @Qualifier(QualifierType.Database)
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository {
        return DatabaseCartRepository(cartProductDao)
    }
}
