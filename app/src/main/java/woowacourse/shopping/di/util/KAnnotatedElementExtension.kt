package woowacourse.shopping.di.util

import woowacourse.shopping.di.Qualifier
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.full.findAnnotation

val KAnnotatedElement.qualifier: String?
    get() = this.findAnnotation<Qualifier>()?.value
