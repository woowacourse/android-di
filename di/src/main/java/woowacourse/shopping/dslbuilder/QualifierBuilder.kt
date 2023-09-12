package woowacourse.shopping.dslbuilder

import woowacourse.shopping.annotation.Qualifier
import kotlin.reflect.KClass

class QualifierBuilder {
    private val qualifiers: MutableMap<Qualifier, KClass<*>> = mutableMapOf()

    fun <T : Any> qualifier(pair: Pair<Qualifier, KClass<T>>) {
        val qualifier = pair.first
        val clazz = pair.second
        qualifiers[qualifier] = clazz
    }

    fun build(): Map<Qualifier, KClass<*>> {
        return qualifiers
    }
}
