package woowacourse.shopping.di.util

import kotlin.reflect.KFunction
import kotlin.reflect.full.primaryConstructor

inline fun <reified T : Any> validateHasPrimaryConstructor(): KFunction<T> {
    val primaryConstructor = T::class.primaryConstructor
    return requireNotNull(primaryConstructor) { "[ERROR] 주생성자가 존재하지 않습니다." }
}
