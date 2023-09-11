package woowacourse.shopping.hashdi

import woowacourse.shopping.hashdi.annotation.Qualifier
import kotlin.reflect.KCallable
import kotlin.reflect.KParameter
import kotlin.reflect.full.hasAnnotation

fun KCallable<*>.identifyKey(): String {
    val type = this.returnType
    val qualifier = this.qualifier()
    return "${type}${qualifier ?: ""}"
}

fun KParameter.identifyKey(): String {
    val type = this.type
    val qualifier = this.qualifier()
    return "${type}${qualifier ?: ""}"
}

fun KCallable<*>.qualifier(): Annotation? {
    return this.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
}

fun KParameter.qualifier(): Annotation? {
    return this.annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
}
