package woowacourse.shopping.di

import kotlin.reflect.KClass

class RepositoryModule(vararg repositories: Pair<KClass<out Any>, KClass<out Any>>) : DependencyProvider {
    private val repositoryContainer = hashMapOf<KClass<out Any>, KClass<out Any>>()
    private val cachedRepository = hashMapOf<KClass<out Any>, Any>()

    init {
        repositories.forEach { (key, repository) ->
            repositoryContainer[key] = repository
        }
    }

    override fun <T : Any> getInstance(key: KClass<*>): T = cachedRepository[key]?.let { it as T } ?: createCachedInstance(key)

    private fun <T : Any> createCachedInstance(key: KClass<*>): T {
        val repositoryClass =
            requireNotNull(repositoryContainer[key]) { "no such repository : $key" }
        val instance = repositoryClass.constructors.first().call()
        cachedRepository[key] = instance
        return instance as T
    }
}
