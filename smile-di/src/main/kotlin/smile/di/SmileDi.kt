package smile.di

import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.cast
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.javaField

class SmileDi(
    private val module: Any,
    private val repository: DependencyRepository,
    private val parent: SmileDi? = null,
) {
    private val providers: Map<DependencyKey, KFunction<*>> =
        module::class
            .declaredMemberFunctions
            .filter { it.hasAnnotation<Provides>() }
            .groupBy { DependencyKey(it.returnType.toKClass(), it.findQualifier()) }
            .mapValues { (key, functions) ->
                check(functions.size == 1) {
                    "[에러] ${key.displayName()} 구현체가 중복 등록되었습니다: ${functions.map { it.name }}"
                }
                functions.single()
            }

    private val creating = mutableSetOf<DependencyKey>()

    fun <T : Any> resolveDependencies(kClass: KClass<T>): T = kClass.cast(resolve(DependencyKey(kClass)))

    fun <T : Any> createDependency(kClass: KClass<T>): T = kClass.cast(create(DependencyKey(kClass)))

    private fun resolve(key: DependencyKey): Any =
        repository.get(key)
            ?: if (parent != null && key !in providers) {
                parent.resolve(key)
            } else {
                create(key).also { repository.save(key, it) }
            }

    private fun create(key: DependencyKey): Any {
        check(creating.add(key)) { "[에러] 순환참조가 발생했습니다." }
        try {
            val provider = findProvider(key)
            return if (provider != null) {
                provider.call(module, *resolveArguments(provider.valueParameters))!!
            } else {
                val constructor =
                    key.kClass.primaryConstructor
                        ?: error("[에러] 클래스의 주생성자가 없습니다.")
                constructor.call(*resolveArguments(constructor.parameters)).also(::injectField)
            }
        } finally {
            creating.remove(key)
        }
    }

    fun createChild(
        module: Any,
        repository: DependencyRepository,
    ): SmileDi = SmileDi(module, repository, parent = this)

    private fun findProvider(key: DependencyKey): KFunction<*>? {
        providers[key]?.let { return it }

        val candidates = providers.keys.filter { it.kClass == key.kClass }
        if (key.qualifier == null) {
            check(candidates.isEmpty()) {
                "[에러] 구현체가 여러 개일 수 없습니다. Qualifier를 지정하세요"
            }
        } else {
            error("[에러] 구현체가 등록되지 않았습니다.")
        }
        return null
    }

    private fun injectField(instance: Any) {
        instance::class
            .memberProperties
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property ->
                val mutable =
                    property.javaField
                        ?: error("[에러] backing property가 존재하지 않아 주입할 수 없습니다.")
                mutable.isAccessible = true
                mutable.set(instance, resolve(property.toDependencyKey(property.returnType)))
            }
    }

    private fun resolveArguments(parameters: List<KParameter>): Array<Any> =
        parameters
            .map {
                resolve(it.toDependencyKey(it.type))
            }.toTypedArray()
}

private fun KType.toKClass(): KClass<*> = this.classifier as? KClass<*> ?: error("[에러] classifier가 KTypeParameter인 경우는 처리할 수 없습니다")

private fun KAnnotatedElement.toDependencyKey(type: KType): DependencyKey = DependencyKey(type.toKClass(), findQualifier())

private fun KAnnotatedElement.findQualifier(): KClass<out Annotation>? {
    val qualifiers =
        annotations
            .map { it.annotationClass }
            .filter { it.hasAnnotation<Qualifier>() }
    check(qualifiers.size <= 1) { "[에러] Qualifier는 하나만 붙일 수 있습니다: $qualifiers" }
    return qualifiers.firstOrNull()
}

private fun DependencyKey.displayName(): String = listOfNotNull(qualifier?.let { "@${it.simpleName}" }, kClass.simpleName).joinToString(" ")
