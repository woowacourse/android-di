package woowacourse.shopping.data.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class RepositoryContainer(
    private val dataContainer: DataContainer,
    private val bindings: List<RepositoryBinding>,
) {
    private val bindingsByKey =
        bindings.associateBy { RepositoryKey(it.type, it.qualifier) }

    private val repositoryStore = mutableMapOf<RepositoryKey, Any>()

    private val resolving = mutableSetOf<RepositoryKey>()

    init {
        require(bindingsByKey.size == bindings.size) {
            "중복된 Repository 바인딩"
        }
        bindings.forEach { binding ->
            require(binding.type.java.isAssignableFrom(binding.implementation.java)) {
                "${binding.implementation}은 ${binding.type}의 구현체가 아닙니다."
            }
        }
    }

    fun inject(target: Any) {
        target::class.java.declaredFields
            .filter{ it.isAnnotationPresent(CustomFieldInjection::class.java) }
            .forEach{ field ->
                val qualifier = field.annotations
                    .asIterable()
                    .qualifierOrNull()

                val dependency = getInstance(field.type.kotlin, qualifier)

                field.isAccessible = true
                field.set(target, dependency)
            }
    }

    fun getInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null
    ): Any {
        val binding = findBinding(type, qualifier)
        val key = RepositoryKey(binding.type, binding.qualifier)
        repositoryStore[key]?.let { return it }

        check(resolving.add(key)) {
            "순환 의존성 발생: $key"
        }

        return try {
            createInstance(binding.implementation).also {
                repositoryStore[key] = it
            }
        } finally {
            resolving.remove(key)
        }
    }

    private fun createInstance(type: KClass<*>): Any {
        require(!type.java.isInterface && !Modifier.isAbstract(type.java.modifiers)) {
            "인터페이스와 추상 클래스는 Repository 타입이 될 수 없음"
        }

        val constructor = type.primaryConstructor
            ?: error("주 생성자를 찾을 수 없음")

        val arguments = constructor.parameters.associateWith { parameter ->
            val dependencyType = parameter.type.classifier as? KClass<*>
                ?: error("생성자 파라미터 타입을 확인할 수 없음: $type ${parameter.name}")
            val qualifier = parameter.annotations.qualifierOrNull()
            resolveDependency(dependencyType, qualifier)
        }

        return constructor.callBy(arguments)
    }

    private fun resolveDependency(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): Any {
        if(qualifier == null) {
            dataContainer.getInstanceOrNull(type)?.let { return it }
        }
        return getInstance(type, qualifier)
    }

    private fun findBinding(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): RepositoryBinding {
        val candidates = bindings.filter { it.type == type }

        if (qualifier == null && candidates.size > 1) {
            error("모호한 Repository 타입, Qualifier를 지정")
        }

        return bindingsByKey[RepositoryKey(type, qualifier)]
            ?: if (qualifier == null && candidates.any { it.qualifier != null }) {
                error("Qualifier가 지정되지 않음")
            } else {
                error("등록되지 않은 Repositor 타입: $type, qualifier: $qualifier")
            }
    }

    private fun Iterable<Annotation>.qualifierOrNull(): KClass<out Annotation>? {
        val qualifiers = mapNotNull { annotation ->
            annotation.annotationClass.takeIf {
                it.java.isAnnotationPresent(Qualifier::class.java)
            }
        }

        require(qualifiers.size <= 1) {
            "주입 지점에는 Qualifier를 하나만 지정해야 함"
        }

        return qualifiers.singleOrNull()
    }
}
