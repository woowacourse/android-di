package woowacourse.shopping.ui

import android.app.Application
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.di.AppContainer
import woowacourse.shopping.model.CartRepository
import woowacourse.shopping.model.ProductRepository
import kotlin.reflect.KType
import kotlin.reflect.full.createType

class ShoppingApplication :
    Application(),
    AppContainer {
    private val dependencies: Map<KType, Any> =
        mapOf(
            ProductRepository::class.createType() to DefaultProductRepository(),
            CartRepository::class.createType() to DefaultCartRepository(),
        )

    override fun dependency(type: KType): Any = dependencies[type] ?: error("Don't know how to inject $type")
}
