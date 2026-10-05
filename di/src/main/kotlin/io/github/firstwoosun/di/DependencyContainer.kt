package io.github.firstwoosun.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

class DependencyContainer(
    private val instanceProvider: InstanceProvider,
    bindings: List<DependencyBinding>,
): AutoCloseable {
    private val bindingsByKey =
        bindings.associateBy { DependencyKey(it.type, it.qualifier) }

    private val bindings = bindings.toList()

    val applicationScope = DependencyScope(ScopeKind.Application)

    init {
        require(bindingsByKey.size == bindings.size) {
            "중복된 의존성 바인딩"
        }
        bindings.forEach { binding ->
            require(binding.type.java.isAssignableFrom(binding.implementation.java)) {
                "${binding.implementation}은 ${binding.type}의 구현체가 아닙니다."
            }
        }
    }

    fun inject(
        target: Any,
        scope: DependencyScope = applicationScope,
    ) {
        scope.checkOpen()

        generateSequence(target::class.java) { it.superclass }
            .takeWhile { it != Any::class.java }
            .flatMap { it.declaredFields.asSequence() }
            .filter { it.isAnnotationPresent(CustomFieldInjection::class.java) }
            .forEach { field ->
                val qualifier = field.annotations.asIterable().qualifierOrNull()
                val dependency = getInstance(
                    type = field.type.kotlin,
                    qualifier = qualifier,
                    scope = scope,
                )

                field.isAccessible = true
                field.set(target, dependency)
            }
    }

    fun getInstance(
        type: KClass<*>,
        qualifier: KClass<out Annotation>? = null,
        scope: DependencyScope = applicationScope
    ): Any {
        scope.checkOpen()

        val binding = findBinding(type, qualifier)
        val key = DependencyKey(binding.type, binding.qualifier)

        val owner = scope.findOwner(binding.scopeKind)

        return owner.getOrCreate(key) {
            createInstance(
                type = binding.implementation,
                scope = owner,
            )
        }
    }

    private fun createInstance(
        type: KClass<*>,
        scope: DependencyScope,
    ): Any {
        require(!type.java.isInterface && !Modifier.isAbstract(type.java.modifiers)) {
            "인터페이스와 추상 클래스는 인스턴스로 생성할 수 없음"
        }

        val constructor =
            type.primaryConstructor
                ?: error("주 생성자를 찾을 수 없음")

        val arguments =
            constructor.parameters.associateWith { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("생성자 파라미터 타입을 확인할 수 없음: $type ${parameter.name}")

                val qualifier = parameter.annotations.qualifierOrNull()

                resolveDependency(
                    dependencyType,
                    qualifier,
                    scope,
                )
            }

        return constructor.callBy(arguments)
    }

    private fun resolveDependency(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
        scope: DependencyScope,
    ): Any {
        if (qualifier == null) {
            instanceProvider.getInstanceOrNull(type)?.let { return it }
        }
        return getInstance(
            type,
            qualifier,
            scope,
        )
    }

    private fun findBinding(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DependencyBinding {
        bindingsByKey[DependencyKey(type, qualifier)]?.let { return it }

        val candidates = bindings.filter { it.type == type }
        val candidateQualifiers = candidates.map { it.qualifier }

        if (qualifier == null && candidates.size > 1) {
            error("모호한 의존성 타입: $type, 후보 qualifier: $candidateQualifiers, Qualifier를 지정")
        }

        if (qualifier == null && candidates.any { it.qualifier != null }) {
            error("Qualifier가 지정되지 않음: $type, 후보 qualifier: $candidateQualifiers")
        }

        error("등록되지 않은 의존성 타입: $type, qualifier: $qualifier, 후보 qualifier: $candidateQualifiers")
    }

    private fun Iterable<Annotation>.qualifierOrNull(): KClass<out Annotation>? {
        val qualifiers =
            mapNotNull { annotation ->
                annotation.annotationClass.takeIf {
                    it.java.isAnnotationPresent(Qualifier::class.java)
                }
            }

        require(qualifiers.size <= 1) {
            "주입 지점에는 Qualifier를 하나만 지정해야 함"
        }

        return qualifiers.singleOrNull()
    }

    override fun close() {
        applicationScope.close()
    }
}
