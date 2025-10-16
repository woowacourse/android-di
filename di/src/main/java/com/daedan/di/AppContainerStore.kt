package com.daedan.di

import com.daedan.di.annotation.Component
import com.daedan.di.annotation.Inject
import com.daedan.di.qualifier.Qualifier
import com.daedan.di.scope.CreateRule
import com.daedan.di.scope.Scope
import com.daedan.di.scope.SingleTonScope
import com.daedan.di.util.getQualifier
import java.util.Collections
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible

class AppContainerStore {
    private val cache =
        Collections.synchronizedMap<Scope, DependencyContainer>(
            mutableMapOf(),
        )

    private val factory = mutableMapOf<Qualifier, DependencyFactory<*>>()

    private val inProgress =
        Collections.synchronizedSet<Qualifier>(
            mutableSetOf(),
        )

    fun registerFactory(vararg modules: DependencyModule) {
        val newFactories = modules.flatMap { it.factories }

        // Map으로 변환
        val newFactoryMap = newFactories.associateBy { it.qualifier }
        require(newFactoryMap.size == newFactories.size) { ERR_CONFLICT_KEY }

        val conflictingKeys = newFactoryMap.keys.filter { factory.containsKey(it) }
        require(conflictingKeys.isEmpty()) {
            "$ERR_CONFLICT_KEY ${conflictingKeys.joinToString()}"
        }

        factory.putAll(newFactoryMap)
    }

    fun instantiate(
        qualifier: Qualifier,
        scope: Scope = SingleTonScope,
    ): Any {
        val scopedContainer = cache.getOrPut(scope) { DependencyContainer() }

        if (scopedContainer.containsKey(qualifier)) {
            return scopedContainer[qualifier] ?: error("$ERR_CANNOT_FIND_INSTANCE : $qualifier")
        }

        val creator = factory[qualifier] ?: error("$ERR_CONSTRUCTOR_NOT_FOUND : $qualifier")

        if (creator.scope != scope && creator.scope != SingleTonScope) {
            error("$ERR_ILLEGAL_SCPE_REQUESTED : $qualifier")
        }

        if (inProgress.contains(qualifier)) {
            error("$ERR_CIRCULAR_DEPENDENCY_DETECTED : $qualifier")
        }

        inProgress.add(qualifier)
        try {
            val instance = creator.invoke()
            injectField(instance)
            creator.scope.save(qualifier, instance)
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
            .filter { it.isTargetField() }
            .forEach { property ->
                val childQualifier = property.getQualifier()
                property.isAccessible = true
                property.set(
                    instance,
                    instantiate(
                        childQualifier,
                    ),
                )
            }
    }

    private fun KMutableProperty1<*, *>.isTargetField(): Boolean =
        findAnnotation<Inject>() != null ||
            annotations.any {
                it.annotationClass.findAnnotation<Component>() != null
            }

    private fun Scope.save(
        qualifier: Qualifier,
        instance: Any,
    ) {
        val createRule = factory[qualifier]?.createRule ?: error("$ERR_CONSTRUCTOR_NOT_FOUND : $qualifier")
        when (createRule) {
            CreateRule.SINGLE -> {
                val container = cache.getOrPut(this) { DependencyContainer() }
                container[qualifier] = instance
            }
            CreateRule.FACTORY -> Unit
        }
    }

    companion object {
        private const val ERR_ILLEGAL_SCPE_REQUESTED = "등록된 스코프와 다른 스코프에서는 객체 생성이 불가능합니다"
        private const val ERR_CONFLICT_KEY = "이미 동일한 Qualifier가 존재합니다"
        private const val ERR_CANNOT_FIND_INSTANCE = "컨테이너에서 인스턴스를 찾을 수 없습니다"
        private const val ERR_CIRCULAR_DEPENDENCY_DETECTED = "순환 참조가 발견되었습니다"
        private const val ERR_CONSTRUCTOR_NOT_FOUND =
            "등록된 팩토리, 또는 주 생성자를 찾을 수 없습니다"
    }
}
