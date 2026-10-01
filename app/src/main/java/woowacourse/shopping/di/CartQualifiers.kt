package woowacourse.shopping.di

import woowacourse.di.Qualifier

@Qualifier
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomCart

@Qualifier
@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryCart
