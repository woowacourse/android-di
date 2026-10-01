package woowacourse.shopping.di

import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.cast
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

class SmileDi(
    private val module: Any,
    private val repository: DependencyRepository,
) {
    private val providers: Map<KClass<*>, KFunction<*>> =
        module::class.declaredMemberFunctions
            .filter { it.hasAnnotation<Provides>() }
            .associateBy { it.returnType.toKClass() }

    private val creating = mutableSetOf<KClass<*>>()

    fun <T : Any> resolveDependencies(kClass: KClass<T>): T {
        return repository.get(kClass) ?: createDependency(kClass).also {
            repository.save(
                kClass,
                it
            )
        }
    }

    fun <T : Any> createDependency(kClass: KClass<T>): T {
        check(creating.add(kClass)) { "[에러] 순환참조가 발생했습니다." }
        try {
            val provider = providers[kClass]
            val instance = if (provider != null) {
                provider.call(module, *resolveArguments(provider.valueParameters))
            } else {
                val constructor = kClass.primaryConstructor
                    ?: error("[에러] $kClass 를 생성할 방법이 없습니다.")
                constructor.call(*resolveArguments(constructor.parameters)).also(::injectField)
            }
            return kClass.cast(instance)
        } finally {
            creating.remove(kClass)
        }
    }

    private fun injectField(instance: Any) {
        instance::class.memberProperties
            .filter { it.hasAnnotation<Inject>() }
            .forEach { property ->
                val mutable = property.javaField
                    ?: error("[에러] backing property가 존재하지 않아 주입할 수 없습니다.")
                mutable.isAccessible = true
                mutable.set(instance, resolveDependencies(property.returnType.toKClass()))
            }
    }

    private fun resolveArguments(parameters: List<KParameter>): Array<Any> {
        return parameters.map { resolveDependencies(it.type.toKClass()) }.toTypedArray()
    }
}

private fun KType.toKClass(): KClass<*> {
    return this.classifier as? KClass<*> ?: error("[에러] classifier가 KTypeParameter인 경우는 처리할 수 없습니다")
}
