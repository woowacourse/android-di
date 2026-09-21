package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartProductDao
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor



object DependencyContainer : ViewModelProvider.Factory {

    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Inject

    private val dependencies: MutableMap<KClass<*>, Any> =
        mutableMapOf(
            ProductRepository::class to ProductRepository(),
        )

    private fun resolve(type: KClass<*>): Any {
        dependencies[type]?.let { return it }

        val constructor =
            type.primaryConstructor
                ?: throw IllegalArgumentException(
                    "No primary constructor for ${type.qualifiedName}",
                )

        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "Unsupported parameter: ${parameter.name}",
                        )

                resolve(dependencyType)
            }

        return constructor.call(*arguments.toTypedArray()).also { instance ->
            injectFields(instance)
            dependencies[type] = instance
        }
    }

    private fun injectFields(instance: Any) {
        instance::class.java.declaredFields
            .filter { field ->
                field.isAnnotationPresent(Inject::class.java)
            }
            .forEach { field ->
                val dependency = resolve(field.type.kotlin)

                field.isAccessible = true
                field.set(instance, dependency)
            }
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = modelClass.kotlin
            .primaryConstructor
            ?.call()
            ?: throw IllegalArgumentException()

        injectFields(viewModel)

        return modelClass.cast(viewModel)
    }

    fun registerCartProductDao(cartProductDao: CartProductDao) {
        dependencies[CartProductDao::class] = cartProductDao
    }
}
