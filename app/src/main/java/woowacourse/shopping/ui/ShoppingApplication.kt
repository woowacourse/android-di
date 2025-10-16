package woowacourse.shopping.ui

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.core.di.DependencyContainer
import woowacourse.shopping.core.di.createInstance
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.InMemoryCartRepository
import woowacourse.shopping.data.PersistentCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.InjectInMemoryCartRepository
import woowacourse.shopping.di.InjectPersistentCartRepository
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class ShoppingApplication :
    Application(),
    DependencyContainer {
    private val dependencies: MutableMap<Pair<KType, List<Annotation>>, Any> = mutableMapOf()

    override fun dependency(
        type: KType,
        qualifiers: List<Annotation>,
    ): Any =
        dependencies.getOrPut(type to qualifiers) {
            when (type) {
                ProductRepository::class.createType() -> createInstance<DefaultProductRepository>()
                CartRepository::class.createType() -> {
                    when {
                        qualifiers.any { it.annotationClass == InjectPersistentCartRepository::class } -> {
                            createInstance<PersistentCartRepository>()
                        }

                        qualifiers.any { it.annotationClass == InjectInMemoryCartRepository::class } -> {
                            createInstance<InMemoryCartRepository>()
                        }

                        else -> error("Don't know how to inject $type")
                    }
                }

                CartProductDao::class.createType() -> {
                    val database: ShoppingDatabase =
                        Room
                            .databaseBuilder(
                                this,
                                ShoppingDatabase::class.java,
                                "cartProduct",
                            ).build()

                    database.cartProductDao()
                }

                else -> error("Don't know how to inject $type")
            }
        }
}
