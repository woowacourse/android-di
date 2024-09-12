package woowacourse.shopping.di

import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.local.LocalCartRepository
import kotlin.reflect.KClass

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class Qualifier(val injectedClassType: KClass<*>)

@Qualifier(LocalCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideLocalCartRepository

@Qualifier(DefaultCartRepository::class)
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class ProvideDefaultCartRepository
