package com.daedan.compactAndroidDi

import com.daedan.compactAndroidDi.annotation.AutoViewModel
import com.daedan.compactAndroidDi.annotation.Inject
import java.util.Collections
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

class AppContainerStore {
    private val cache: MutableMap<Qualifier, Any> = mutableMapOf()
    private val factory = mutableMapOf<Qualifier, DependencyFactory<*>>()

    private val inProgress =
        Collections.synchronizedSet<Qualifier>(
            mutableSetOf(),
        )

    operator fun get(clazz: KClass<*>): Any? = cache[Qualifier(clazz)]

    fun registerFactory(vararg factories: DependencyFactory<*>) {
        factory.putAll(factories.associateBy { it.qualifier })
    }

    fun instantiate(qualifier: Qualifier): Any {
        if (cache.containsKey(qualifier)) {
            return cache[qualifier] ?: throw IllegalArgumentException(
                "$ERR_CONSTRUCTOR_NOT_FOUND : ${qualifier.type.simpleName}",
            )
        }

        if (inProgress.contains(qualifier)) {
            throw IllegalArgumentException(
                "$ERR_CIRCULAR_DEPENDENCY_DETECTED : ${qualifier.type.simpleName}",
            )
        }

        inProgress.add(qualifier)
        val instance =
            factory[qualifier]?.invoke()
                ?: reflect(qualifier.type)
                ?: throw IllegalArgumentException("$ERR_CONSTRUCTOR_NOT_FOUND : ${qualifier.type.simpleName}")

        injectField(instance)
        save(qualifier, instance)
        inProgress.remove(qualifier)
        return instance
    }

    private fun reflect(type: KClass<*>): Any? {
        if (type.findAnnotation<AutoViewModel>() == null) return null
        val constructor = type.primaryConstructor ?: return null
        val arguments =
            constructor.parameters
                .filter { !it.isOptional }
                .associateWith { parameter ->
                    val childQualifier =
                        Qualifier(
                            parameter.type.jvmErasure,
                            parameter.findAnnotation<Inject>()?.name,
                        )

                    instantiate(
                        childQualifier,
                    )
                }
        return constructor.callBy(arguments)
    }

    private fun injectField(instance: Any) {
        try {
            instance::class.memberProperties
        } catch (e: Error) {
            // 코틀린 리플렉션이 지원하지 않는 프레임워크 객체는 건너뜁니다
            if (e::class.simpleName == "KotlinReflectionInternalError") return
        }
        instance::class
            .memberProperties
            .filterIsInstance<KMutableProperty1<Any, Any>>()
            .filter { it.findAnnotation<Inject>() != null }
            .forEach { property ->
                val childQualifier =
                    Qualifier(
                        property.returnType.jvmErasure,
                        property.findAnnotation<Inject>()?.name,
                    )
                property.set(
                    instance,
                    instantiate(
                        childQualifier,
                    ),
                )
            }
    }

    private fun save(
        qualifier: Qualifier,
        instance: Any,
    ) {
        if (qualifier.type.findAnnotation<AutoViewModel>() != null) return
        cache[qualifier] = instance
    }

    companion object {
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "등록된 팩토리, 또는 주 생성자를 찾을 수 없습니다"
    }
}
