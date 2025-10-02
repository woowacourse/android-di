package woowacourse.shopping.di

import kotlin.reflect.KType

interface AppContainer {
    fun resolve(type: KType): Any
}
