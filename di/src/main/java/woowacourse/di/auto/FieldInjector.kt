package woowacourse.di.auto

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object FieldInjector {
    @Suppress("UNCHECKED_CAST")
    fun inject(
        target: Any,
        container: Container,
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.findAnnotation<InjectField>() != null }
            .forEach { property ->
                val type =
                    (property.returnType.classifier as? KClass<*>)
                        ?: error("Unsupported field type: ${property.returnType}")
                val qualifier = findQualifier(property)
                val value = container.get(type, qualifier)
                property.isAccessible = true
                property.set(target, value)
            }
    }
}
