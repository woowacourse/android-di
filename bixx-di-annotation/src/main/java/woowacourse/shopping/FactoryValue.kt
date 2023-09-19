package woowacourse.shopping

import kotlin.reflect.KFunction

data class FactoryValue(
    val function: KFunction<*>,
    val factory: Any,
)
