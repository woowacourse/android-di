package woowacourse.shopping.di

import woowacourse.shopping.data.CartInMemoryRepository

@Qualifier(CartInMemoryRepository::class)
@Retention(AnnotationRetention.SOURCE)
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.VALUE_PARAMETER,
    AnnotationTarget.PROPERTY,
)
annotation class InMemory
