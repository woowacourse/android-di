package woowacourse.shopping.dslbuilder

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class ProviderBuilder {
    private val providers: MutableMap<KClass<*>, KFunction<*>> = mutableMapOf()

    fun <T : Any> provider(pair: Pair<KClass<T>, KFunction<T>>) {
        val clazz = pair.first
        val provider = pair.second
        providers[clazz] = provider
    }

    fun build(): Map<KClass<*>, KFunction<*>> {
        return providers
    }
}
