package woowacourse.shopping.di

import kotlin.reflect.KClass

class RepositoryContainer private constructor(
    private val store: HashMap<KClass<*>, Any> = hashMapOf(),
) : Container {

    override fun getInstance(type: KClass<*>): Any? {
        return store[type]
    }

    override fun setInstance(type: KClass<*>, instance: Any) {
        store[type] = instance
    }

    companion object {
        fun of(vararg injection: Pair<KClass<*>, Any>): RepositoryContainer {
            return RepositoryContainer(hashMapOf(*injection))
        }
    }
}
