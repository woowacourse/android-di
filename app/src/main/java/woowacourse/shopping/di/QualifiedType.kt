package woowacourse.shopping.di

import kotlin.reflect.KType

data class QualifiedType(
    val returnType: KType,
    val qualifier: String? = null,
)
