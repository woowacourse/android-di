package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
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
                CartRepository::class.createType() -> instance<DefaultCartRepository>()
                else -> error("Don't know how to inject $type")
            }
        }
}
