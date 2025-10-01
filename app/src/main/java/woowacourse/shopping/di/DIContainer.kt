package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartDefaultRepository
import woowacourse.shopping.data.repository.ProductDefaultRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIContainer {
    private val instances = mutableMapOf<String, Any>()

    fun getInstance(kClass: KClass<*>): Any {
        val typeName = kClass.simpleName!!

        val existingInstance = instances[typeName]
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
        registerRepository("ProductRepository", ProductDefaultRepository())
        registerRepository("CartRepository", CartDefaultRepository())
    }

    private fun registerRepository(
        typeName: String,
        instance: Any,
    ) {
        instances[typeName] = instance
    }

    companion object {
        val instance: DIContainer by lazy {
            DIContainer().apply {
                setupRepositories()
            }
        }
    }
}
