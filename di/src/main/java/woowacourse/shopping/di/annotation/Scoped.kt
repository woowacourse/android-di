package woowacourse.shopping.di.annotation

import woowacourse.shopping.di.Scope

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Scoped(
    val scope: Scope,
)
