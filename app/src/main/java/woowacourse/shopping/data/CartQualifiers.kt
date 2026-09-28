package woowacourse.shopping.data

import woowacourse.shopping.di.KirbyQualifier

@KirbyQualifier
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class RoomCart

@KirbyQualifier
@Target(AnnotationTarget.PROPERTY, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class InMemoryCart
