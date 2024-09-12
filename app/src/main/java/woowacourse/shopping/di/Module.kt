package woowacourse.shopping.di

import kotlin.reflect.KClass

interface Module {
    fun provideInstance(dependencyRegistry: DependencyRegistry)

    fun findQualifierOrNull(classType: KClass<*>): KClass<*>?
}
