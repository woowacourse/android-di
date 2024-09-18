package olive.di.util

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

internal fun KFunction<*>.toReturnType(): KClass<*> = returnType.classifier as KClass<*>

internal fun KFunction<*>.parameters(): List<KClass<*>> {
    return parameters
        .map { it.type.classifier as KClass<*> }
        .filter { this != it }
}
