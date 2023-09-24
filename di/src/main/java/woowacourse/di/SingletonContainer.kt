package woowacourse.di

import kotlin.reflect.KClass

object SingletonContainer {

    val instances = mutableMapOf<KClass<*>, Any>()
}
