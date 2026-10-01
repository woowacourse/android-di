package woowacourse.di

import java.lang.annotation.RetentionPolicy
import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.primaryConstructor

class Injector {
    private val providers = mutableMapOf<DependencyKey, Injector.() -> Any>()
    private val singletons = mutableMapOf<DependencyKey, Any>()
    private val resolvingKeys = mutableListOf<DependencyKey>()

    @Synchronized
    fun <T : Any> registerSingleton(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
        provider: Injector.() -> T,
    ) {
        qualifier?.let(::validateQualifier)
        val key = DependencyKey(type, qualifier)
        require(key !in providers && key !in singletons) {
            "이미 등록되거나 생성된 의존성입니다: $key"
        }
        providers[key] = provider
    }

    @Synchronized
    fun <T : Any> create(
        type: KClass<T>,
        qualifier: KClass<out Annotation>? = null,
    ): T {
        val key = resolveKey(type, qualifier)
        singletons[key]?.let { return type.cast(it) }
        check(key !in resolvingKeys) {
            val path = (resolvingKeys + key).joinToString(" -> ")
            "순환 의존성이 발견되었습니다: $path"
        }
        resolvingKeys.add(key)
        try {
            val instance =
                providers[key]?.let { type.cast(it(this)) }
                    ?: type.objectInstance
                    ?: construct(type)
            injectFields(instance)
            if (key in providers || type.objectInstance != null) {
                singletons[key] = instance
            }
            return instance
        } finally {
            resolvingKeys.removeAt(resolvingKeys.lastIndex)
        }
    }

    private fun resolveKey(
        type: KClass<*>,
        qualifier: KClass<out Annotation>?,
    ): DependencyKey {
        qualifier?.let(::validateQualifier)
        val key = DependencyKey(type, qualifier)
        val candidates = providers.keys.filter { it.type == type }.sortedBy { it.toString() }
        require(qualifier != null || candidates.size <= 1) {
            "여러 의존성이 등록되어 Qualifier가 필요합니다: ${type.simpleName}. 등록된 후보: $candidates"
        }
        if (qualifier != null) {
            require(key in providers) {
                "등록되지 않은 의존성입니다: $key. 등록된 후보: $candidates"
            }
        } else {
            require(candidates.isEmpty() || key in providers) {
                "등록된 의존성의 Qualifier를 지정해야 합니다: ${type.simpleName}. 등록된 후보: $candidates"
            }
        }
        return key
    }

    private fun validateQualifier(qualifier: KClass<out Annotation>) {
        require(qualifier.findAnnotation<Qualifier>() != null) {
            "@Qualifier가 붙은 애노테이션만 사용할 수 있습니다: ${qualifier.simpleName}"
        }
        val retention = qualifier.java.getAnnotation(java.lang.annotation.Retention::class.java)
        require(retention?.value == RetentionPolicy.RUNTIME) {
            "Qualifier는 RUNTIME 보존 정책이 필요합니다: ${qualifier.simpleName}"
        }
        require(qualifier.java.declaredMethods.isEmpty()) {
            "Qualifier는 값 없이 애노테이션 타입으로 구분해야 합니다: ${qualifier.simpleName}"
        }
    }

    private fun qualifierOn(
        annotations: List<Annotation>,
        target: String,
    ): KClass<out Annotation>? {
        val qualifiers =
            annotations.map { it.annotationClass }.filter { it.findAnnotation<Qualifier>() != null }
        require(qualifiers.size <= 1) {
            "주입 대상에는 Qualifier를 하나만 지정해야 합니다: $target. 발견된 Qualifier: $qualifiers"
        }
        return qualifiers.singleOrNull()
    }

    private fun <T : Any> construct(type: KClass<T>): T {
        require(!type.isAbstract) {
            "인터페이스나 추상 클래스의 생성 방법을 등록해야 합니다: ${type.simpleName}"
        }
        val constructor =
            requireNotNull(type.primaryConstructor) {
                "${type.simpleName}의 주 생성자를 찾을 수 없습니다."
            }
        val arguments =
            constructor.parameters.map { parameter ->
                val dependencyType =
                    parameter.type.classifier as? KClass<*>
                        ?: error("지원하지 않는 타입: ${parameter.type}")
                val qualifier = qualifierOn(parameter.annotations, "${type.simpleName}.${parameter.name}")
                create(dependencyType, qualifier)
            }
        return constructor.call(*arguments.toTypedArray())
    }

    private fun injectFields(instance: Any) {
        generateSequence<Class<*>>(instance.javaClass) { it.superclass }
            .flatMap { it.declaredFields.asSequence() }
            .filter { it.isAnnotationPresent(Inject::class.java) }
            .forEach { field ->
                require(!Modifier.isFinal(field.modifiers) && !Modifier.isStatic(field.modifiers)) {
                    "@Inject는 변경 가능한 인스턴스 필드에만 사용할 수 있습니다: ${field.name}"
                }
                val qualifier = qualifierOn(field.annotations.toList(), "${field.declaringClass.simpleName}.${field.name}")
                field.isAccessible = true
                field.set(instance, create(field.type.kotlin, qualifier))
            }
    }
}
