package woowacourse.shopping.data.di

import kotlin.reflect.KClass

data class AnnotationType(val annotation: Annotation? = null, val clazz: KClass<*>)
