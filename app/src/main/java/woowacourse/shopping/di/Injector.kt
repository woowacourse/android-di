package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun <T : Any> inject(
    modelClass: KClass<T>,
    dependencies: DependencyProvider,
): T {
    val constructors = requireNotNull(modelClass.primaryConstructor) { "No suitable constructor found for $modelClass" }
    val arguments =
        constructors.parameters.associateWith { kParameter ->
            val paramsTypes = kParameter.type.classifier as KClass<*>
            dependencies.getInstance<Any>(paramsTypes)
        }
    return constructors.callBy(arguments)
}
