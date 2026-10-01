package woowacourse.shopping.data.annotation

import woowacourse.shopping.di.annotation.Qualifier

@Qualifier
@Target(
    AnnotationTarget.PROPERTY,
    AnnotationTarget.FUNCTION,
)
annotation class InMemoryCart
