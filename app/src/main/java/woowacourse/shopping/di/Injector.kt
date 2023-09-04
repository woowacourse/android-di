package woowacourse.shopping.di

import woowacourse.shopping.data.repository.CartRepository
import woowacourse.shopping.data.repository.ProductRepository
import kotlin.reflect.KParameter
import kotlin.reflect.full.createType
import kotlin.reflect.full.primaryConstructor

object Injector {

    inline fun <reified T : Any> getInstance(): T {
        val constructor = T::class.primaryConstructor ?: throw IllegalStateException()
        val parameters = constructor.parameters
        return constructor.call(*getArguments(parameters).toTypedArray())
    }

    fun getArguments(parameters: List<KParameter>): MutableList<Any> {
        val arguments = mutableListOf<Any>()

        for (param in parameters) {
            arguments.add(createArgument(param))
        }
        return arguments
    }

    private fun createArgument(
        param: KParameter,
    ): Any = when (param.type) {
        ProductRepository::class.createType() ->
            RepositoryContainer.productRepository
        CartRepository::class.createType() ->
            RepositoryContainer.cartRepository
        else -> throw IllegalArgumentException()
    }
}
