package woowacourse.di

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

object FieldInjector {
    @Suppress("UNCHECKED_CAST")
    fun inject(
        target: Any,
        container: Container,
        scopeContext: ScopeContext = ScopeContext.application(),
    ) {
        target::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.hasAnnotation<InjectField>() }
            .forEach { property ->
                val type =
                    (property.returnType.classifier as? KClass<*>)
                        ?: error("Unsupported field type: ${property.returnType}")
                val qualifier = findQualifier(property)
                val value = container.get(type, qualifier, scopeContext)
                property.isAccessible = true
                property.set(target, value)
            }
    }
}
