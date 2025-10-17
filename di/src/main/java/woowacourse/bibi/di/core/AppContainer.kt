package woowacourse.bibi.di.core

import kotlin.reflect.KClass
import kotlin.reflect.KType

interface AppContainer {
    fun resolve(type: KType): Any = resolve(type, null)

    fun resolve(
        type: KType,
        qualifier: KClass<out Annotation>? = null,
    ): Any

    fun child(scope: KClass<out Annotation>): AppContainer

    fun clear()
}
