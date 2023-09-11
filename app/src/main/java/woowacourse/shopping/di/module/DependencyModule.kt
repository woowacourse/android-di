package woowacourse.shopping.di.module

import kotlin.reflect.KClass

interface DependencyModule{
    fun invoke(): Map<KClass<*>, Any>
}