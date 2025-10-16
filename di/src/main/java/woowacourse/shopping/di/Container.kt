package woowacourse.shopping.di

import kotlin.reflect.KType

interface Container {
    fun resolve(
        requestedType: KType,
        qualifier: Annotation?,
    ): Any?
}
