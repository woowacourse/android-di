package woowacourse.shopping.di

import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> validateHasPrimaryConstructor(): KFunction<T> {
    val primaryConstructor = T::class.primaryConstructor
    return requireNotNull(primaryConstructor) { "[ERROR] 주생성자가 존재하지 않습니다." }
}

fun <T> KFunction<T>.validateIncludingInjectAnnotation() {
    annotations.forEach { param ->
        require(isInjectAnnotation(param)) { "[ERROR] Inject 어노테이션이 존재하지 않습니다." }
    }
}

private fun isInjectAnnotation(annotation: Annotation): Boolean = annotation is Inject
