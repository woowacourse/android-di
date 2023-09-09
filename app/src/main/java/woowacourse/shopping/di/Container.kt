package woowacourse.shopping.di

import kotlin.reflect.KClass

interface Container {

    fun find(clazz: KClass<*>): Any?

    fun find(clazzName: String): Any?
}
