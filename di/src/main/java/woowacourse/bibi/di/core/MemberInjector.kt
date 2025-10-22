package woowacourse.bibi.di.core

import kotlin.reflect.full.createType
import kotlin.reflect.full.hasAnnotation

object MemberInjector {
    fun inject(
        target: Any,
        container: Container,
    ) {
        target::class.java.declaredFields
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                field.isAccessible = true
                val kType = field.type.kotlin.createType()
                val qualifier =
                    field.annotations
                        .firstOrNull {
                            it.annotationClass.hasAnnotation<Qualifier>()
                        }?.annotationClass
                val value = container.resolve(kType, qualifier)
                field.set(target, value)
            }
    }
}
