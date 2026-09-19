package woowacourse.shopping.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

object DependencyContainer : ViewModelProvider.Factory {
    private val dependencies: Map<KClass<*>, Any> =
        mapOf(
            ProductRepository::class to ProductRepository(),
            CartRepository::class to CartRepository(),
        )

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val constructor =
            modelClass.kotlin.primaryConstructor
                ?: throw IllegalArgumentException("No primary constructor for ${modelClass.name}")
        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: throw IllegalArgumentException(
                            "Unsupported constructor parameter: ${parameter.name}",
                        )
                dependencies[dependencyType]
                    ?: throw IllegalArgumentException(
                        "No dependency registered for ${dependencyType.qualifiedName}",
                    )
            }
        return modelClass.cast(constructor.call(*arguments.toTypedArray()))!!
    }
}
