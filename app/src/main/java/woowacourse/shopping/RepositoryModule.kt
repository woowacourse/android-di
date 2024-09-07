package woowacourse.shopping

import woowacourse.shopping.di.DependencyProvider
import kotlin.reflect.KClass
import kotlin.reflect.KClassifier

class RepositoryModule(vararg repositories: Pair<KClass<out Any>, Any>) : DependencyProvider {
    private val repositoryContainer = hashMapOf<KClass<out Any>, Any>()

    init {
        repositories.forEach { (key, repository) ->
            repositoryContainer[key] = repository
        }
    }

    override fun <T : Any> getInstance(key: KClassifier): T = requireNotNull(repositoryContainer[key]) { "no such repository : $key" } as T
}
