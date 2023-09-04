package woowacourse.shopping.di

import java.util.concurrent.ConcurrentHashMap
import kotlin.reflect.KClass

typealias Declaration<T> = () -> T

fun module(block: Module.() -> Unit) = Module().apply(block)

// 여러 모듈 생성 가능, 최종적으로 사용할 서비스로 올라가기 전, 모듈의 declarationRegistry 에 임시로 저장한다.
open class Module {
    val declarationRegistry: MutableMap<KClass<*>, Declaration<Any>> = ConcurrentHashMap()

    inline fun <reified T : Any> provide(noinline declaration: Declaration<T>) {
        declarationRegistry[T::class] = declaration
    }

    // 모듈 내에서 의존성 주입이 필요할 때
    inline fun <reified T : Any> get(): T {
        val declaration = declarationRegistry[T::class]
        var instance = declaration?.invoke()

        if (instance == null) {
            instance = Injector.declarations[T::class]?.invoke()
                ?: error("Unable to find declaration of type ${T::class.qualifiedName}")
        }
        return instance as T
    }

    operator fun plus(module: Module) = listOf(module, this)
}
