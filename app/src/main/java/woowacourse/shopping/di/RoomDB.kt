package woowacourse.shopping.di

import woowacourse.shopping.data.CartDefaultRepository
import woowacourse.shopping.di.Qualifier

@Qualifier(CartDefaultRepository::class)
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY,
    AnnotationTarget.EXPRESSION,
    AnnotationTarget.CLASS,
)
annotation class RoomDB
