package woowacourse.shopping

import android.app.Application
import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.data.repository.ProductRepository
import woowacourse.shopping.di.DIContainer
import kotlin.reflect.KClass

class ShoppingApplication : Application() {
    val diContainer by lazy {
        DIContainer(createInterfaceMapping())
    }

    private fun createInterfaceMapping(): Map<KClass<*>, KClass<*>> {
        return mapOf(
            ProductRepository::class to ProductDefaultRepository::class,
            CartRepository::class to CartDefaultRepository::class
        )
    }
}