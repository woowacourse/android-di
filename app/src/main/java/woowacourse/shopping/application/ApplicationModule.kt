package woowacourse.shopping.application

import android.content.Context
import com.example.di.Module
import com.example.di.annotation.Qualifier
import com.example.di.annotation.QualifierType
import woowacourse.shopping.data.repository.DatabaseCartRepository
import woowacourse.shopping.data.repository.InMemoryCartRepository
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.local.dao.CartProductDao
import woowacourse.shopping.local.db.ShoppingDatabase

class ApplicationModule(private val context: Context) : Module {
    @Qualifier(QualifierType.ApplicationContext)
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    fun provideCartProductDao(): CartProductDao {
        return ShoppingDatabase.getInstance(context).cartProductDao()
    }

    @Qualifier(QualifierType.InMemory)
    fun provideInMemoryCartRepository(): CartRepository {
        return InMemoryCartRepository()
    }

    @Qualifier(QualifierType.Database)
    fun provideDatabaseCartRepository(cartProductDao: CartProductDao): CartRepository {
        return DatabaseCartRepository(cartProductDao)
    }
}
