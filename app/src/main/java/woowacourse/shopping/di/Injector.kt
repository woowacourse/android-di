package woowacourse.shopping.di

import java.lang.reflect.Modifier
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.primaryConstructor

class Injector {
    private val providers = mutableMapOf<KClass<*>, Injector.() -> Any>()
    private val singletons = mutableMapOf<KClass<*>, Any>()
    private val resolvingTypes = mutableListOf<KClass<*>>()

    @Synchronized
    fun <T : Any> registerSingleton(
        type: KClass<T>,
        provider: Injector.() -> T,
    ) {
        require(type !in providers && type !in singletons) {
            "이미 등록되거나 생성된 의존성입니다: ${type.simpleName}"
        }
        providers[type] = provider
    }

    @Synchronized
    fun <T : Any> create(type: KClass<T>): T {
        singletons[type]?.let { return type.cast(it) }
        check(type !in resolvingTypes) {
            val path = (resolvingTypes + type).joinToString(" -> ") { it.simpleName.orEmpty() }
            "순환 의존성이 발견되었습니다: $path"
        }
        resolvingTypes.add(type)
        try {
            val instance =
                providers[type]?.let { type.cast(it(this)) }
                    ?: type.objectInstance
                    ?: construct(type)
            injectFields(instance)
            if (type in providers || type.objectInstance != null) {
                singletons[type] = instance
            }
            return instance
        } finally {
            resolvingTypes.removeAt(resolvingTypes.lastIndex)
        }
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
                create(dependencyType)
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
                field.isAccessible = true
                field.set(instance, create(field.type.kotlin))
            }
    }
}
