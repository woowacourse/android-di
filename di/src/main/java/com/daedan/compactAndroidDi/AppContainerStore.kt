package com.daedan.compactAndroidDi

import com.daedan.compactAndroidDi.annotation.Component
import com.daedan.compactAndroidDi.annotation.Inject
import com.daedan.compactAndroidDi.qualifier.Qualifier
import com.daedan.compactAndroidDi.util.getQualifier
import java.util.Collections
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties

class AppContainerStore {
    private val cache: MutableMap<Qualifier, Any> = mutableMapOf()
    private val factory = mutableMapOf<Qualifier, DependencyFactory<*>>()

    private val inProgress =
        Collections.synchronizedSet<Qualifier>(
            mutableSetOf(),
        )

    operator fun get(qualifier: Qualifier): Any? = cache[qualifier]

    fun registerFactory(vararg modules: DependencyModule) {
        val factories = mutableListOf<DependencyFactory<*>>()
        modules.forEach { factories.addAll(it.factories) }
        factory.putAll(factories.associateBy { it.qualifier })
    }

    fun instantiate(qualifier: Qualifier): Any {
        if (cache.containsKey(qualifier)) {
            return cache[qualifier] ?: error("$ERR_CANNOT_FIND_INSTANCE : $qualifier")
        }

        if (inProgress.contains(qualifier)) {
            error("$ERR_CIRCULAR_DEPENDENCY_DETECTED : $qualifier")
        }

        inProgress.add(qualifier)
        try {
            val instance =
                factory[qualifier]?.invoke()
                    ?: error("$ERR_CONSTRUCTOR_NOT_FOUND : $qualifier")
            injectField(instance)
            cache[qualifier] = instance
            return instance
        } finally {
            inProgress.remove(qualifier)
        }
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
            .filter { it.findAnnotation<Inject>() != null || it.annotations.any { it.annotationClass.findAnnotation<Component>() != null } }
            .forEach { property ->
                val childQualifier = property.getQualifier()
                property.set(
                    instance,
                    instantiate(
                        childQualifier,
                    ),
                )
            }
    }

    companion object {
        private const val ERR_CANNOT_FIND_INSTANCE = "컨테이너에서 인스턴스를 찾을 수 없습니다"
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "등록된 팩토리, 또는 주 생성자를 찾을 수 없습니다"
    }
}
