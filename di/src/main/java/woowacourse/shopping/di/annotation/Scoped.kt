package woowacourse.shopping.di.annotation

import woowacourse.shopping.di.Scope

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FIELD, AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scoped(
    val scope: Scope,
)
