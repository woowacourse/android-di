package woowacourse.shopping.di

import woowacourse.shopping.data.CartRepositoryImpl
import woowacourse.shopping.data.ProductRepositoryImpl
import woowacourse.shopping.domain.CartRepository
import woowacourse.shopping.domain.ProductRepository
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object AppContainer {

    val recipe: MutableList<Pair<KClass<*>, KClass<*>>> = mutableListOf()

    val repositories : MutableList<KClass<*>> = mutableListOf()


    init {
        register(CartRepository::class, CartRepositoryImpl::class)
        register(ProductRepository::class, ProductRepositoryImpl::class)
    }

    fun register(repository: KClass<*>, impl: KClass<*>) {
        recipe.add(repository to impl)
    }


    inline fun <reified T : Any> get(): T {
        val constructor = T::class.primaryConstructor?: throw IllegalArgumentException()

        val args = constructor.parameters.map{ parameter ->
            val paramType = parameter.type.classifier as? KClass<*>
            val impl = recipe.find{ it.first == paramType}?: throw IllegalArgumentException()
            return@map impl.second.primaryConstructor?.call()
        }
        return constructor.call(*args.toTypedArray())
    }
}