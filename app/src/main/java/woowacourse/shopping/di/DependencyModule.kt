package woowacourse.shopping.di

import kotlin.reflect.KClass

interface DependencyModule{
    fun invoke(): Map<KClass<*>, Any>
}