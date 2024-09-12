package woowacourse.shopping.di

import kotlin.reflect.KClass

annotation class Qualifier(val classifier: KClass<*>)
