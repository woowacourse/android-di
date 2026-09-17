package woowacourse.shopping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

object Storage {
    val cartRepository: CartRepository = CartRepository()
    val productRepository: ProductRepository = ProductRepository()
}

object DiFactory {
    fun resolve(modelClass: KClass<*>): Any {
        val constructor = modelClass.primaryConstructor!!
        val types = constructor.parameters.map { it.type.classifier as KClass<*> }
        val repos = Storage::class.memberProperties

        val inst = types.map { type ->
            val property = repos.single {
                it.returnType.classifier == type
            }

            property.getter.call(Storage)
        }

        return constructor.call(*inst.toTypedArray())
    }

    fun viewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return resolve(modelClass.kotlin) as T
            }
        }
    }
}