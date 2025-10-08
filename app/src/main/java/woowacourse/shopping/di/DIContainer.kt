package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import woowacourse.shopping.data.repository.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val instances = mutableMapOf<KClass<*>, Any>()

    fun getInstance(kClass: KClass<*>): Any {
        val existingInstance = instances[kClass]
        if (existingInstance != null) {
            return existingInstance
        }

        return createNewInstance(kClass)
    }

    private fun createNewInstance(kClass: KClass<*>): Any {
        val constructor = kClass.primaryConstructor ?: throw IllegalStateException()
        val parameters = mutableListOf<Any>()

        constructor.parameters.forEach { param ->
            val paramTypeName = param.type.classifier as KClass<*>
            val paramInstance = getInstance(paramTypeName)
            parameters.add(paramInstance)
        }

        return constructor.call(*parameters.toTypedArray())
    }

    private fun setupRepositories() {
        registerRepository<ProductRepository>(ProductDefaultRepository())
        registerRepository<CartRepository>(CartDefaultRepository())
    }

    private inline fun <reified T> registerRepository(instance: T) {
        instances[T::class] = instance as Any
    }

    companion object {
        val instance: DIContainer by lazy {
            DIContainer().apply {
                setupRepositories()
            }
        }
    }
}
