package woowacourse.shopping.di

import kotlin.reflect.KType

interface AppContainer {
    fun dependency(type: KType): Any
}
