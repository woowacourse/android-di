package woowacourse.di.annotation

import kotlin.reflect.KClass

annotation class Qualifier(val clazz: KClass<*>)
