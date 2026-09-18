package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()
    val repositories = listOf(productRepository, cartRepository)

    fun findDependencies(types: List<KClass<*>>): List<Any> =
        repositories.filter {
            types.contains(it::class)
        }
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        val repos = DIContainer.findDependencies(types)
        return constructor.call(*repos.toTypedArray())
    }
}
