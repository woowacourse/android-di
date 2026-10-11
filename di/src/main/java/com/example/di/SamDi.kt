package com.example.di

import com.example.di.annotations.InjectField
import com.example.di.annotations.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

class SamDi(
    private val providers: Map<KClass<*>, () -> Any>,
    private val bindings: Map<Pair<KClass<*>, KClass<*>>, KClass<*>>,
    private val scopes: Map<KClass<*>, String> = emptyMap(),
    private val rootScope: Scope? = null,
) {
    fun resolve(
        modelClass: KClass<*>,
        annotation: Annotation? = null,
        scope: Scope? = rootScope,
    ): Any {
        val implType =
            if (annotation != null) {
                bindings[modelClass to annotation.annotationClass]
                    ?: throw IllegalArgumentException(
                        " ${annotation.annotationClass}, $modelClass 와 연결된 정보를 찾을 수 없습니다",
                    )
            } else {
                val candidates = bindings.filterKeys { (type, _) -> type == modelClass }
                when (candidates.size) {
                    0 -> modelClass
                    1 -> candidates.values.single()
                    else -> throw IllegalArgumentException(
                        "$modelClass 에 대해 여러 후보지가 있습니다. qualifier를 명확히 하세요",
                    )
                }
            }

        check(scope?.isClosed != true) { "종료된 스코프입니다" }
        val scopeKind = scopes[implType] ?: scopes[modelClass]
        val owner = scopeKind?.let { kind ->
            requireNotNull(scope) { "필요한 스코프가 없습니다: $kind" }.owner(kind)
        }
        val create = { create(implType, owner ?: scope) }
        return owner?.getOrCreate(modelClass to annotation?.annotationClass, create) ?: create()
    }

    private fun create(implType: KClass<*>, scope: Scope?): Any {
        providers[implType]?.let { return it() }
        val constructor = implType.primaryConstructor
            ?: throw IllegalArgumentException("요청 타입: $implType 가 없음")
        val dependencies = constructor.parameters.map { parameter ->
            val type = parameter.type.classifier as KClass<*>
            val qualifier = parameter.annotations.find {
                it.annotationClass.hasAnnotation<Qualifier>()
            }
            resolve(type, qualifier, scope)
        }
        return constructor.call(*dependencies.toTypedArray())
    }

    fun injectFields(
        instance: Any,
        scope: Scope? = rootScope,
    ) {
        instance::class.memberProperties
            .filter { it.findAnnotation<InjectField>() != null }
            .filterIsInstance<KMutableProperty1<*, *>>()
            .forEach { property ->
                val type = property.returnType.classifier as KClass<*>

                val qualifier = property.annotations.find {
                    it.annotationClass.hasAnnotation<Qualifier>()
                }
                val value = resolve(type, qualifier, scope)
                property.setter.call(instance, value)
            }
    }
}
