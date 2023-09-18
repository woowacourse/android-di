package woowacourse.shopping.di.module

import android.content.Context
import com.boogiwoogi.di.Modules
import com.boogiwoogi.di.Provides
import com.boogiwoogi.di.Qualifier
import woowacourse.shopping.data.DatabaseCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import woowacourse.shopping.ui.cart.DateFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.functions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.jvm.jvmErasure

class ShoppingApplicationModules(private val context: Context) : Modules {

    @Qualifier("ApplicationContext")
    @Provides
    fun provideApplicationContext(): Context {
        return context.applicationContext
    }

    @Qualifier("ActivityContext")
    @Provides
    fun provideActivityContext(): Context {
        if (context == context.applicationContext) {
            throw IllegalArgumentException(CONTEXT_TYPE_ERROR)
        }
        return context
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

    @Provides
    fun provideProductRepository(): ProductRepository {
        return DefaultProductRepository()
    }

    @Provides
    fun provideDateFormatter(): DateFormatter {
        return DateFormatter(context)
    }

    override fun provideInstanceOf(clazz: KClass<*>): Any? {
        val function = this::class
            .functions
            .filter { it.hasAnnotation<Provides>() }
            .firstOrNull { it.returnType.jvmErasure == clazz }

        return function?.call(this)
    }

    override fun provideInstanceOf(simpleName: String): Any? {
        val function = this::class
            .functions
            .filter { it.hasAnnotation<Qualifier>() }
            .firstOrNull { it.findAnnotation<Qualifier>()!!.simpleName == simpleName }

        return function?.call(this)
    }

    companion object {
        private const val CONTEXT_TYPE_ERROR = "inappropriate context"
    }
}
