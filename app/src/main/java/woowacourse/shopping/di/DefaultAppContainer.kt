package woowacourse.shopping.di

import android.content.Context
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.data.ShoppingDatabase
import woowacourse.shopping.domain.repository.CartRepository
import woowacourse.shopping.domain.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DefaultAppContainer : AppContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

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

    fun init(context: Context) {
        bind(ShoppingDatabase::class, ShoppingDatabase.getInstance(context))
        bind(CartProductDao::class, getInstance(ShoppingDatabase::class).cartProductDao())
        bind(ProductRepository::class, getInstance(ProductRepositoryImpl::class))
        bind(CartRepository::class, getInstance(DefaultCartRepository::class))
    }

    private fun <T : Any> bind(
        clazz: KClass<T>,
        instance: T,
    ) = instances.putIfAbsent(clazz, instance)
}
