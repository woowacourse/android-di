package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DefaultAppContainer : AppContainer {
    private val productRepository: ProductRepository by lazy { ProductRepositoryImpl() }
    private val cartRepository: CartRepository by lazy { CartRepositoryImpl() }
    private val instances: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to productRepository,
            CartRepository::class to cartRepository,
        )

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> getInstance(clazz: KClass<T>): T {
        instances[clazz]?.let { return it as T }

        val constructor =
            requireNotNull(clazz.primaryConstructor) {
                "${clazz.qualifiedName} 클래스에 생성자가 존재하지 않습니다."
            }

        val args =
            constructor.parameters
                .map { param ->
                    val depClass =
                        requireNotNull(param.type.classifier as KClass<*>) {
                            "지원하지 않는 타입입니다: ${param.type}"
                        }
                    getInstance(depClass)
                }.toTypedArray()

        return constructor.call(*args)
    }
}
