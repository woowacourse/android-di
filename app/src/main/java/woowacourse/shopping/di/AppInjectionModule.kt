package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import java.util.concurrent.ConcurrentHashMap

object AppInjectionModule {
    private val instances = ConcurrentHashMap<Class<*>, Any>()

    private val mapper =
        mapOf<Class<*>, Class<*>>(
            ProductRepository::class.java to ProductRepositoryImpl::class.java,
            CartRepository::class.java to CartRepositoryImpl::class.java,
        )

    fun <T : Any> getOrCreate(clazz: Class<T>): T {
        val target = mapper[clazz] ?: clazz

        val instance = instances.getOrPut(target) { createInstance(target) }
        if (!clazz.isInstance(instance)) throw IllegalStateException("cast 불가능 find = ${instance::class.java} target = ${clazz.name}")
        return clazz.cast(instance) as T
    }

    private fun <T : Any> createInstance(clazz: Class<T>): Any {
        val constructor =
            clazz.declaredConstructors.firstOrNull()
                ?: throw IllegalArgumentException("인스턴스화 불가능 클래스 class = ${clazz.name}")

        val params = constructor.parameterTypes.map { getOrCreate(it) }.toTypedArray()
        return constructor.newInstance(*params)
    }
}
