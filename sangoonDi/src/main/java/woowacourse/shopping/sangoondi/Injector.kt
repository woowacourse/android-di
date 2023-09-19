package woowacourse.shopping.sangoondi

import woowacourse.shopping.sangoondi.DiContainer.modules
import woowacourse.shopping.sangoondi.annotation.Field
import woowacourse.shopping.sangoondi.annotation.Qualifier
import woowacourse.shopping.sangoondi.annotation.Singleton
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.full.valueParameters
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmErasure

object Injector {
    const val INVALID_CLASS_TYPE = "[ERROR] Its class type cannot be injected"
    private const val INVALID_KEY = "[ERROR] Cannot find value with a matching key"
    private const val INVALID_FUNCTION = "[ERROR] Function not found in any module"

    inline fun <reified T : Any> inject(): T {
        val clazz = T::class
        val constructors =
            clazz.primaryConstructor ?: throw IllegalStateException(INVALID_CLASS_TYPE)

        val instances = constructors.parameters.map { kParameter ->
            getSingletonIfInstantiate(kParameter) ?: kParameter.toInstance()
        }

        return constructors.call(*instances.toTypedArray()).apply {
            injectField(this)
        }
    }

    inline fun <reified T : Any> injectField(clazz: T) {
        val properties = T::class.declaredMemberProperties.filter { property ->
            property.hasAnnotation<Field>()
        }

        if (properties.isEmpty()) return

        properties.forEach { property ->
            val instance = getSingletonIfInstantiate(property as KParameter)
                ?: (property as KParameter).toInstance()

            property.isAccessible = true
            property.javaField?.set(clazz, instance)
        }
    }

    fun getSingletonIfInstantiate(kParameter: KParameter): Any? =
        DiContainer.singletonInstance[kParameter.type.jvmErasure]

    fun KParameter.toInstance(): Any {
        val func = when (annotations.any { it.hasQualifier() }) {
            true -> DiContainer.qualifiedInstance[annotations.filter { it.hasQualifier() }]
            false -> DiContainer.instance[type]
        } ?: throw IllegalArgumentException(INVALID_KEY)

        val module = modules.find { module ->
            module::class.declaredFunctions.any {
                it.annotations.any { it.hasQualifier() } == func.annotations.any { it.hasQualifier() }
            }
        } ?: throw IllegalArgumentException(INVALID_FUNCTION)

        val arg = func.valueParameters.takeIf { it.isNotEmpty() }?.map {
            it.toInstance()
        }?.toTypedArray() ?: emptyArray()

        return (func.call(module, *arg) ?: throw IllegalArgumentException()).also {
            it.toSingletonIfSingleton(func)
        }
    }

    private fun Annotation.hasQualifier() = this.annotationClass.hasAnnotation<Qualifier>()

    private fun Any.toSingletonIfSingleton(func: KFunction<*>) {
        if (func.hasAnnotation<Singleton>()) DiContainer.setSingleton(this, this::class)
    }
}

// 앱 컨텍스트
// 나머지 요구사항
