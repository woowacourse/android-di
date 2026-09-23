package woowacourse.shopping.data.di

import woowacourse.shopping.data.CartRepository
import woowacourse.shopping.data.DefaultCartRepository
import woowacourse.shopping.data.ProductRepository
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class RepositoryContainer(
    private val dataContainer: DataContainer
) {
    private val binding = mapOf(
        CartRepository::class to DefaultCartRepository::class,
        ProductRepository::class to ProductRepository::class
    )

    private val repositoryStore = mutableMapOf<KClass<*>, Any>()

    private val resolving = mutableSetOf<KClass<*>>()

    fun inject(target: Any) {
        target::class.java.declaredFields
            .filter { field ->
                field.isAnnotationPresent(CustomFieldInjection::class.java)
            }
            .forEach { field ->
                val dependencyType = field.type.kotlin
                val dependency = getInstance(dependencyType)

                field.isAccessible = true
                field.set(target, dependency)
            }
    }

    fun getInstance(type: KClass<*>): Any {
        repositoryStore[type]?.let { return it }

        val implementationType =
            binding[type]
                ?: error("등록되지 않은 Repository 타입")

        repositoryStore[implementationType]?.let {
            repositoryStore[type] = it
            return it
        }

        check(resolving.add(implementationType)) {
            "순환 의존성 발생"
        }

        val instance = createInstance(implementationType)
        repositoryStore[type] = instance
        repositoryStore[implementationType] = instance

        resolving.remove(implementationType)

        return instance
    }

    private fun createInstance(type: KClass<*>): Any {
        require(!type.java.isInterface && !Modifier.isAbstract(type.java.modifiers)) {
            "인터페이스와 추상 클래스는 Repository 타입이 될 수 없음"
        }

        val constructor = type.primaryConstructor ?: error("주 생성자를 찾을 수 없음")

        val arguments =
            constructor.parameters.associateWith { parameter ->
                val dependencyType = parameter.type.classifier as? KClass<*>
                    ?: error("생성자 파라미터 타입을 확인할 수 없음: $type ${parameter.name}")
                resolveDependency(dependencyType)
            }

        return constructor.callBy(arguments)
    }

    private fun resolveDependency(type: KClass<*>): Any {
        return dataContainer.getInstanceOrNull(type) ?: getInstance(type)
    }
}
