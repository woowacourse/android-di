package woowacourse.shopping.di

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
annotation class Inject

@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Qualifier(val clazz: KClass<out Any>)
