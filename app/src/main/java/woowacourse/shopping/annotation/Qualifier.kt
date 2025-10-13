package woowacourse.shopping.annotation

import kotlin.reflect.KClass

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val value: KClass<out Any>)
