package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.DIContainer.repositories
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.jvm.kotlin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DIContainer {
    private val productRepository = ProductRepository()
    private val cartRepository = CartRepository()
    val repositories = listOf(productRepository, cartRepository)
}

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor = modelClass.kotlin.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        val repos = repositories.filter { types.contains(it::class) }
        return constructor.call(*repos.toTypedArray())
    }
}
