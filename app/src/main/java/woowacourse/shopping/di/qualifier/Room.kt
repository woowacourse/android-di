package woowacourse.shopping.di.qualifier

import woowacourse.di.Qualifier

@Qualifier
@Target(AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Room
