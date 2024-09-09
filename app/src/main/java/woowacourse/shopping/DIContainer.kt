package woowacourse.shopping

import android.util.Log
import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.DefaultProductRepository
import woowacourse.shopping.data.ProductRepository
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class DIContainer(
    val dependencies: Map<KClass<*>, Any> = mapOf(
        ProductRepository::class to DefaultProductRepository,
        CartRepository::class to DefaultCartRepository,
    )
) {
    inline fun <reified T : Any> createViewModel(
        modelClass: KClass<T>,
        vararg any: Any? = emptyArray()
    ): T {
        var index = 0
        val primaryConstructor = modelClass.primaryConstructor ?: return modelClass.createInstance()
        val constructorArgs = primaryConstructor.parameters.map { parameter ->
            if (parameter.hasAnnotation<Inject>()) {
                val dependencyClass = parameter.type.classifier as? KClass<*>
                dependencies[dependencyClass]
            } else {
                any[index++]
            }
        }.toTypedArray()

        return primaryConstructor.call(*constructorArgs)
    }
}
