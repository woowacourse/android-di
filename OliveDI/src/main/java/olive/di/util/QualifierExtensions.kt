package olive.di.util

import olive.di.annotation.Qualifier
import kotlin.reflect.KClass

internal fun List<Annotation>.hasQualifierAnnotation(): Boolean {
    return any { annotation -> annotation.isQualifierAnnotation() }
}

internal fun List<Annotation>.qualifierNameAnnotation(): KClass<out Annotation> {
    val nameAnnotation = first { functionAnnotation -> functionAnnotation.isQualifierAnnotation() }
    return nameAnnotation.annotationClass
}

internal fun Annotation.isQualifierAnnotation(): Boolean {
    return annotationClass.annotations.any { it.annotationClass == Qualifier::class }
}
