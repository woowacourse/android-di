package woowacourse.shopping.di.auto

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
        val kClass = target::class
        kClass.memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any?>>()
            .filter { it.findAnnotation<InjectField>() != null }
            .forEach { property ->
                val kParameter =
                    property.returnType.classifier as? KClass<*>
                        ?: error("Unsupported field type: ${property.returnType}")
                val value = container.get(kParameter)
                property.isAccessible = true
                property.set(target, value)
            }
    }
}
