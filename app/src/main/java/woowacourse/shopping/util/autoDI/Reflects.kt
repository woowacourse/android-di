package woowacourse.shopping.util.autoDI

import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.jvm.reflect

fun <T> getLambdaReturnType(lambda: () -> T): KType? {
    val reflectedLambda = lambda.reflect() ?: return null
    return (reflectedLambda as? KFunction<*>)?.returnType
}
