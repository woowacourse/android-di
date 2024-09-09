package woowacourse.shopping

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DIApplication : Application() {
    val repositories =
        mapOf<KClass<*>?, Any>(
            ProductRepository::class to DefaultProductRepository,
            CartRepository::class to DefaultCartRepository,
        )

    inline fun <reified T : ViewModel> createViewModelByDI(modelClass: Class<T>): ViewModelProvider.Factory =
        viewModelFactory {
            val primaryConstructor = modelClass.kotlin.primaryConstructor
                ?: return@viewModelFactory initializer { modelClass as T }

            val constructorArgs =
                primaryConstructor.parameters.map { parameter ->
                    val dependencyClass = parameter.type.classifier as? KClass<*>
                    repositories[dependencyClass]
                }.toTypedArray()

            initializer {
                primaryConstructor.call(*constructorArgs)
            }
        }
}
