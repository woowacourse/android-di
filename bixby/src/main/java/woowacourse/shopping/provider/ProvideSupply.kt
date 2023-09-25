package woowacourse.shopping.provider

import kotlin.reflect.KFunction

// provide 함수의 반환 타입을 key로, KFunction 형태의 provide 함수와 해당 함수가 있는 Factory를 value로 갖는다.
// 즉 무언가를 provide 하는데 필요한 준비물이다
data class ProvideSupply(
    val function: KFunction<*>,
    val factory: Any,
)
