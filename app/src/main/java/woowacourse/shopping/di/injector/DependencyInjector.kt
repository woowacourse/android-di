package woowacourse.shopping.di.injector

import woowacourse.shopping.Module
import woowacourse.shopping.di.annotation.Inject
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KType
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.starProjectedType
import kotlin.reflect.jvm.jvmErasure

// 3. Key : (어노테이션, 함수 반환 타입)
//    Value : 함수
data class Qualify(
    val module: Module,
    val type: KType, // 반환 타입 (인터페이스일 수도 있음)
    val annotation: Annotation? = null, // 그런 경우를 대비해 어노테이션도 함께 저장
)

object DependencyInjector {
    val dependencies = mutableMapOf<Qualify, KFunction<*>>()
    val types = mutableMapOf<KType, KType>()
    private val cache = mutableMapOf<Qualify, Any>()

    fun <T : Any> inject(clazz: KClass<T>): T {
        val primaryConstructor =
            clazz.primaryConstructor ?: throw IllegalArgumentException("주생성자 없음")
        val parameters = primaryConstructor.parameters
        val arguments = parameters.map { parameter ->
            val hasQualifiedAnnotation = parameter.annotations.any(::isAnnotationPresent)
            val isSameType = isSameType(parameter.type)

            if (hasQualifiedAnnotation && isSameType) {
                val qualify = dependencies.keys.find { qualify ->
                    qualify.annotation == parameter.annotations.first() &&
                        qualify.type == parameter.type
                }
                val cached = cache[qualify]
                if (cached != null) {
                    return@map cached
                }
                val instance = dependencies[qualify]?.call(qualify!!.module)
                cache[qualify!!] = instance!!
                instance
            } else if (isSameType) {
                val qualify = dependencies.keys.find { qualify ->
                    qualify.type == parameter.type
                }
                val cached = cache[qualify]
                if (cached != null) {
                    return@map cached
                }
                val instance = dependencies[qualify]?.call(qualify!!.module)
                    ?: throw IllegalArgumentException("의존성 주입 실패")
                cache[qualify!!] = instance
                instance
            } else {
                val childType = types[parameter.type] ?: throw IllegalArgumentException("의존성 주입 실패")
                inject(childType.classifier as KClass<*>)
            }
        }

        val instance = primaryConstructor.call(*arguments.toTypedArray())
        clazz.java.declaredFields.forEach { field ->
            if (field.isAnnotationPresent(Inject::class.java)) {
                field.isAccessible = true
                field.set(instance, inject(types[field.type.kotlin.starProjectedType]!!.jvmErasure))
            }
        }

        return instance
    }

    private fun isAnnotationPresent(annotation: Annotation): Boolean {
        return dependencies.keys.any { qualify -> qualify.annotation == annotation }
    }

    private fun isSameType(type: KType): Boolean {
        return dependencies.keys.any { qualify -> qualify.type == type }
    }
}

fun modules(
    vararg modules: Module,
) {
    modules.forEach { module: Module ->
        module::class.declaredMemberFunctions.forEach { function ->
            if (function.hasAnnotation<Annotation>()) {
                val qualify = Qualify(
                    module,
                    function.returnType,
                    function.annotations.first(),
                )
                DependencyInjector.dependencies[qualify] = function
            } else {
                val qualify = Qualify(module, function.returnType)
                DependencyInjector.dependencies[qualify] = function
            }
        }
    }
}

fun type(
    vararg types: Pair<KClass<*>, KClass<*>>,
) {
    types.forEach { (parentType, childType) ->
        DependencyInjector.types[parentType.starProjectedType] = childType.starProjectedType
    }
}
