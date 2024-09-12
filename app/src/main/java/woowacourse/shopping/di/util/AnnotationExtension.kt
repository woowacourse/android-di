package woowacourse.shopping.di.util

import woowacourse.shopping.di.Qualifier
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty1
import kotlin.reflect.full.hasAnnotation

fun KParameter.getAnnotationIncludeQualifier() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }

fun <T> KProperty1<T, *>.getAnnotationIncludeQualifier() = annotations.firstOrNull { it.annotationClass.hasAnnotation<Qualifier>() }
