package woowacourse.shopping

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.di.DIContainer
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass

class ShoppingApplication : Application() {
    private val database by lazy {
        Room
            .databaseBuilder(
                applicationContext,
                ShoppingDatabase::class.java,
                "shopping_database",
            ).build()
    }

    val diContainer by lazy {
        DIContainer(createInterfaceMapping(), database)
    }

    private fun createInterfaceMapping(): Map<KClass<*>, KClass<*>> =
        mapOf(
            ProductRepository::class to ProductDefaultRepository::class,
            CartRepository::class to CartDefaultRepository::class,
        )
}
