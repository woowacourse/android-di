package woowacourse.shopping.app.di // app 모듈에 있으나, di 패키지명은 유지

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.di.Container
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.KType

class AppContainerImpl(
    private val applicationContext: Context,
) : Container {
    private val database: ShoppingDatabase by lazy {
        ShoppingDatabase.getInstance(applicationContext)
    }

    val cartProductDao: CartProductDao by lazy {
        database.cartProductDao()
    }

    override val productRepository: ProductRepository by lazy { DefaultProductRepository() }
    override val cartRepository: CartRepository by lazy { DefaultCartRepository(cartProductDao) }

    override fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any? {
        val kClass = requestedType.classifier as? KClass<*> ?: return null
        val qualifierKClass = qualifier?.annotationClass

        return when (kClass) {
            ProductRepository::class -> {
                when (qualifierKClass?.simpleName) {
                    "Database" -> productRepository
                    else -> productRepository
                }
            }

            CartRepository::class -> {
                when (qualifierKClass?.simpleName) {
                    "Database" -> cartRepository
                    else -> cartRepository
                }
            }

            else -> null
        }
    }
}
