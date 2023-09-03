package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.repository.CartRepository
import woowacourse.shopping.repository.ProductRepository
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.javaType

class DependencyProvider private constructor() {
    private val dependencies = mutableMapOf<Class<*>, Any>()

    init {
        dependencies[ProductRepository::class.java] = DefaultProductRepository()
        dependencies[CartRepository::class.java] = DefaultCartRepository()
    }

    fun <T : Any>createInstance(clazz: KClass<T>): T {
        val insertParameters: MutableList<Any> = mutableListOf()
        val primaryConstructor = clazz.primaryConstructor ?: throw IllegalArgumentException()

        val parameters = primaryConstructor.parameters
        parameters.forEach {
            insertParameters.add(dependencies[it.type.javaType]!!)
        }

        return clazz.primaryConstructor?.call(*insertParameters.toTypedArray())!!
    }

    companion object {
        private val instance = DependencyProvider()

        fun getInstance(): DependencyProvider {
            return instance
        }
    }
}
