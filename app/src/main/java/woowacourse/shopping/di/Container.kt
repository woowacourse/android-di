package woowacourse.shopping.di

import kotlin.reflect.KClass

interface Container {

    fun find(kClass: KClass<*>): Any?
}
