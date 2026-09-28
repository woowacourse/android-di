package com.example.di

import com.example.di.annotations.Qualifier
import kotlin.reflect.KClass
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor

class SamDi(
    private val providers: Map<KClass<*>, () -> Any>,
    private val bindings: Map<Pair<KClass<*>, KClass<*>>, KClass<*>>,
) {
    fun resolve(
        modelClass: KClass<*>,
        annotation: Annotation? = null,
    ): Any {
        providers[modelClass]?.let { provider ->
            return provider()
        }

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

        val constructor =
            implType.primaryConstructor
                ?: throw IllegalArgumentException("요청 타입: $modelClass 가 없음")
        val dependencies = constructor.parameters.map { parameter ->
            val type = parameter.type.classifier as KClass<*>
            val qualifier = parameter.annotations.find { parameterAnnotation ->
                parameterAnnotation.annotationClass.hasAnnotation<Qualifier>()
            }
            resolve(type, qualifier)
        }

        return constructor.call(*dependencies.toTypedArray())
    }
}
