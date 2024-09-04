package woowacourse.shopping

import kotlin.reflect.KClass

class AppContainer(vararg repositories: Pair<KClass<out Any>, Any>) {
    private val repositoryContainer = hashMapOf<KClass<out Any>, Any>()

    init {
        repositories.forEach { (key, repository) ->
            repositoryContainer[key] = repository
        }
    }

    fun <T : Any> getRepository(key: KClass<T>): T = requireNotNull(repositoryContainer[key]) { "no such repository : $key" } as T
}
