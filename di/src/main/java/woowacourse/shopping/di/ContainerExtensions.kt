package woowacourse.shopping.di

import kotlin.reflect.typeOf

inline fun <reified T : Any> Container.resolve(qualifier: Annotation? = null): T? = resolve(typeOf<T>(), qualifier) as? T
