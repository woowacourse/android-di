package woowacourse.shopping

import woowacourse.shopping.data.CartDefaultRepository

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
