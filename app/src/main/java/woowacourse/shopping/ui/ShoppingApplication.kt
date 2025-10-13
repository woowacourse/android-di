package woowacourse.shopping.ui

import android.app.Application
import androidx.room.Room
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.RoomCartRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.di.instance
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class ShoppingApplication :
    Application(),
    AppContainer {
    private val dependencies: MutableMap<KType, Any> = mutableMapOf()

    override fun dependency(type: KType): Any =
        dependencies.getOrPut(type) {
            when (type) {
                ProductRepository::class.createType() -> instance<DefaultProductRepository>()
                CartRepository::class.createType() -> instance<RoomCartRepository>()
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
