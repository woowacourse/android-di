package woowacourse.shopping.di.model

import kotlin.reflect.KClass

data class InstanceIdentifier(
    val type: KClass<*>,
    val qualifier: List<Annotation>,
)
